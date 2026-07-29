package com.aiassisted.urlshortener.controller;

import com.aiassisted.urlshortener.model.UrlMapping;
import com.aiassisted.urlshortener.repository.UrlMappingRepository;
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

    public RedirectController(UrlMappingRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{token}")
    public ResponseEntity<Void> redirect(@PathVariable String token, HttpServletRequest request) {
        UrlMapping urlMapping = repository.findByToken(token)
                .or(() -> repository.findByAlias(token))
                .orElseThrow(() -> new IllegalArgumentException("Short URL not found."));

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(urlMapping.getLongUrl()));
        return ResponseEntity.status(302).headers(headers).build();
    }
}
