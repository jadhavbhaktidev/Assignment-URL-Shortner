package com.aiassisted.urlshortener.controller;

import com.aiassisted.urlshortener.dto.MetricsResponse;
import com.aiassisted.urlshortener.exception.ResourceNotFoundException;
import com.aiassisted.urlshortener.model.UrlMapping;
import com.aiassisted.urlshortener.repository.UrlMappingRepository;
import com.aiassisted.urlshortener.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MetricsController {

    private final UrlMappingRepository repository;
    private final AnalyticsService analyticsService;

    public MetricsController(UrlMappingRepository repository,
                             AnalyticsService analyticsService) {
        this.repository = repository;
        this.analyticsService = analyticsService;
    }

    @GetMapping("/urls/{id}/metrics")
    public ResponseEntity<MetricsResponse> getMetrics(@PathVariable("id") Long id) {
        UrlMapping urlMapping = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("URL not found."));
        MetricsResponse response = new MetricsResponse(
            urlMapping.getId(),
            urlMapping.getClicksCount(),
            analyticsService.countUniqueVisitors(urlMapping.getId())
        );
        return ResponseEntity.ok(response);
    }
}
