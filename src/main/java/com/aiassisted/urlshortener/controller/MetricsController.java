package com.aiassisted.urlshortener.controller;

import com.aiassisted.urlshortener.dto.MetricsResponse;
import com.aiassisted.urlshortener.model.UrlMapping;
import com.aiassisted.urlshortener.repository.UrlMappingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MetricsController {

    private final UrlMappingRepository repository;
    private final String apiKey;

    public MetricsController(UrlMappingRepository repository, @Value("${urlshortener.api-key}") String apiKey) {
        this.repository = repository;
        this.apiKey = apiKey;
    }

    @GetMapping("/urls/{id}/metrics")
    public ResponseEntity<MetricsResponse> getMetrics(@PathVariable Long id, @RequestHeader(value = "X-API-KEY", required = false) String headerApiKey) {
        if (!apiKey.equals(headerApiKey)) {
            return ResponseEntity.status(401).build();
        }

        UrlMapping urlMapping = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("URL not found."));
        MetricsResponse response = new MetricsResponse(urlMapping.getId(), urlMapping.getClicksCount(), 0L);
        return ResponseEntity.ok(response);
    }
}
