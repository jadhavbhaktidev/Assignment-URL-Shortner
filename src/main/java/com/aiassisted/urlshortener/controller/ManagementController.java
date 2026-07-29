package com.aiassisted.urlshortener.controller;

import com.aiassisted.urlshortener.repository.UrlMappingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ManagementController {

    private final UrlMappingRepository repository;

    public ManagementController(UrlMappingRepository repository) {
        this.repository = repository;
    }

    @DeleteMapping("/urls/{id}")
    public ResponseEntity<Void> deleteUrl(@PathVariable Long id) {
        return repository.findById(id)
                .map(url -> {
                    repository.delete(url);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
