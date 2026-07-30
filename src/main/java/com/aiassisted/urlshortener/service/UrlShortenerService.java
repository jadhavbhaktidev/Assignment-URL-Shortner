package com.aiassisted.urlshortener.service;

import com.aiassisted.urlshortener.dto.ShortenRequest;
import com.aiassisted.urlshortener.dto.ShortenResponse;
import com.aiassisted.urlshortener.exception.ConflictException;
import com.aiassisted.urlshortener.model.UrlMapping;
import com.aiassisted.urlshortener.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UrlShortenerService {

    private final UrlMappingRepository repository;
    private final SecureRandom secureRandom = new SecureRandom();

    public UrlShortenerService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    public ShortenResponse shortenUrl(ShortenRequest request) {
        String alias = request.getCustomAlias();
        if (StringUtils.hasText(alias)) {
            if (repository.existsByAlias(alias) || repository.existsByToken(alias)) {
                throw new ConflictException("Custom alias is already in use.");
            }
        } else {
            alias = generateUniqueToken();
        }

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setToken(alias);
        urlMapping.setAlias(request.getCustomAlias());
        urlMapping.setLongUrl(request.getLongUrl());
        UrlMapping saved = repository.save(urlMapping);

        return new ShortenResponse(saved.getId(), alias, alias);
    }

    private String generateUniqueToken() {
        for (int attempt = 0; attempt < 5; attempt++) {
            String token = randomToken();
            if (!repository.existsByToken(token)) {
                return token;
            }
        }
        throw new IllegalStateException("Unable to generate a unique token. Please try again.");
    }

    private String randomToken() {
        byte[] buffer = new byte[6];
        secureRandom.nextBytes(buffer);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buffer);
    }
}
