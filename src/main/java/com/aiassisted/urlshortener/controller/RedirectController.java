package com.aiassisted.urlshortener.controller;

import com.aiassisted.urlshortener.exception.ResourceNotFoundException;
import com.aiassisted.urlshortener.model.UrlMapping;
import com.aiassisted.urlshortener.repository.UrlMappingRepository;
import com.aiassisted.urlshortener.service.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class RedirectController {

    private final UrlMappingRepository repository;
    private final AnalyticsService analyticsService;

    public RedirectController(UrlMappingRepository repository, AnalyticsService analyticsService) {
        this.repository = repository;
        this.analyticsService = analyticsService;
    }

    @GetMapping("/{token}")
    public ResponseEntity<Void> redirect(@PathVariable("token") String token, HttpServletRequest request) {
        UrlMapping urlMapping = repository.findByToken(token)
                .or(() -> repository.findByAlias(token))
            .orElseThrow(() -> new ResourceNotFoundException("Short URL not found."));

        analyticsService.recordEvent(urlMapping, request.getRemoteAddr(), request.getHeader("User-Agent"), request.getHeader("Referer"), null);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(urlMapping.getLongUrl()));
        return ResponseEntity.status(302).headers(headers).build();
    }
}
