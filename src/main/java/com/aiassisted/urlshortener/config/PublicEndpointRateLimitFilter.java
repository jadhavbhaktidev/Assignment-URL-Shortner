package com.aiassisted.urlshortener.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PublicEndpointRateLimitFilter extends OncePerRequestFilter {

    private static final class Bucket {
        private final AtomicInteger counter = new AtomicInteger(0);
        private volatile long windowStartEpochMilli;

        private Bucket(long windowStartEpochMilli) {
            this.windowStartEpochMilli = windowStartEpochMilli;
        }
    }

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final boolean enabled;
    private final int maxRequests;
    private final long windowMillis;

    public PublicEndpointRateLimitFilter(
            @Value("${urlshortener.rate-limit.enabled:true}") boolean enabled,
            @Value("${urlshortener.rate-limit.max-requests:120}") int maxRequests,
            @Value("${urlshortener.rate-limit.window-seconds:60}") int windowSeconds) {
        this.enabled = enabled;
        this.maxRequests = maxRequests;
        this.windowMillis = windowSeconds * 1000L;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (!enabled) {
            return true;
        }
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String path = request.getRequestURI();
        boolean isPublicShorten = path.startsWith("/api/v1/shorten");
        boolean isPublicRedirect = !path.startsWith("/api/") && path.matches("^/[^/]+$");
        return !(isPublicShorten || isPublicRedirect);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String clientKey = resolveClientKey(request);
        long now = Instant.now().toEpochMilli();

        Bucket bucket = buckets.computeIfAbsent(clientKey, key -> new Bucket(now));
        synchronized (bucket) {
            if (now - bucket.windowStartEpochMilli >= windowMillis) {
                bucket.windowStartEpochMilli = now;
                bucket.counter.set(0);
            }

            int count = bucket.counter.incrementAndGet();
            if (count > maxRequests) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write("{\"code\":\"RATE_LIMITED\",\"message\":\"Too many requests. Retry later.\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveClientKey(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}