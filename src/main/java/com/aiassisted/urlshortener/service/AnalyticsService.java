package com.aiassisted.urlshortener.service;

import com.aiassisted.urlshortener.model.AggregateCount;
import com.aiassisted.urlshortener.model.Event;
import com.aiassisted.urlshortener.model.UrlMapping;
import com.aiassisted.urlshortener.repository.AggregateCountRepository;
import com.aiassisted.urlshortener.repository.EventRepository;
import com.aiassisted.urlshortener.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
public class AnalyticsService {

    private final EventRepository eventRepository;
    private final AggregateCountRepository aggregateCountRepository;
    private final UrlMappingRepository urlMappingRepository;

    public AnalyticsService(EventRepository eventRepository,
                            AggregateCountRepository aggregateCountRepository,
                            UrlMappingRepository urlMappingRepository) {
        this.eventRepository = eventRepository;
        this.aggregateCountRepository = aggregateCountRepository;
        this.urlMappingRepository = urlMappingRepository;
    }

    public void recordEvent(UrlMapping urlMapping, String ipHash, String userAgent, String referrer, String country) {
        String hashedIp = hashIp(ipHash);
        LocalDate today = LocalDate.now();
        OffsetDateTime startOfDay = today.atStartOfDay().atOffset(OffsetDateTime.now().getOffset());
        OffsetDateTime endOfDay = startOfDay.plusDays(1);
        boolean isUniqueToday = hashedIp != null && !eventRepository
            .existsByUrlMappingAndIpHashAndOccurredAtBetween(urlMapping, hashedIp, startOfDay, endOfDay);

        Event event = new Event();
        event.setUrlMapping(urlMapping);
        event.setIpHash(hashedIp);
        event.setUserAgent(userAgent);
        event.setReferrer(referrer);
        event.setCountry(country);
        eventRepository.save(event);

        AggregateCount aggregateCount = aggregateCountRepository.findByUrlMappingAndAggregateDay(urlMapping, today)
                .orElseGet(() -> {
                    AggregateCount agg = new AggregateCount();
                    agg.setUrlMapping(urlMapping);
                    agg.setAggregateDay(today);
                    return agg;
                });

        aggregateCount.setClicks(aggregateCount.getClicks() + 1);
        if (isUniqueToday) {
            aggregateCount.setUniques(aggregateCount.getUniques() + 1);
        }
        aggregateCountRepository.save(aggregateCount);

        urlMapping.setClicksCount(urlMapping.getClicksCount() + 1);
        urlMappingRepository.save(urlMapping);
    }

    public long countUniqueVisitors(Long urlId) {
        return eventRepository.countDistinctIpHashByUrlMappingId(urlId);
    }

    private String hashIp(String remoteAddress) {
        if (remoteAddress == null || remoteAddress.isBlank()) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(remoteAddress.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 algorithm is unavailable", ex);
        }
    }
}
