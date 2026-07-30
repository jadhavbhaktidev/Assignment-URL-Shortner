package com.aiassisted.urlshortener.controller;

import com.aiassisted.urlshortener.dto.ShortenRequest;
import com.aiassisted.urlshortener.dto.ShortenResponse;
import com.aiassisted.urlshortener.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1")
public class UrlShortenerController {

    private final UrlShortenerService service;

    public UrlShortenerController(UrlShortenerService service) {
        this.service = service;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shorten(@Valid @RequestBody ShortenRequest request, HttpServletRequest httpRequest) {
        ShortenResponse response = service.shortenUrl(request);
        String shortUrl = ServletUriComponentsBuilder.fromRequestUri(httpRequest)
                .replacePath("/{alias}")
                .buildAndExpand(response.getAlias())
                .toUriString();
        response.setShortUrl(shortUrl);
        return ResponseEntity.status(201).body(response);
    }
}
