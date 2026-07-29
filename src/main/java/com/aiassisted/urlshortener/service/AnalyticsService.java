package com.aiassisted.urlshortener.service;

import com.aiassisted.urlshortener.model.AggregateCount;
import com.aiassisted.urlshortener.model.Event;
import com.aiassisted.urlshortener.model.UrlMapping;
import com.aiassisted.urlshortener.repository.AggregateCountRepository;
import com.aiassisted.urlshortener.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AnalyticsService {

    private final EventRepository eventRepository;
    private final AggregateCountRepository aggregateCountRepository;

    public AnalyticsService(EventRepository eventRepository, AggregateCountRepository aggregateCountRepository) {
        this.eventRepository = eventRepository;
        this.aggregateCountRepository = aggregateCountRepository;
    }

    public void recordEvent(UrlMapping urlMapping, String ipHash, String userAgent, String referrer, String country) {
        Event event = new Event();
        event.setUrlMapping(urlMapping);
        event.setIpHash(ipHash);
        event.setUserAgent(userAgent);
        event.setReferrer(referrer);
        event.setCountry(country);
        eventRepository.save(event);

        LocalDate today = LocalDate.now();
        AggregateCount aggregateCount = aggregateCountRepository.findByUrlMappingAndDay(urlMapping, today)
                .orElseGet(() -> {
                    AggregateCount agg = new AggregateCount();
                    agg.setUrlMapping(urlMapping);
                    agg.setDay(today);
                    return agg;
                });

        aggregateCount.setClicks(aggregateCount.getClicks() + 1);
        aggregateCountRepository.save(aggregateCount);
    }
}
