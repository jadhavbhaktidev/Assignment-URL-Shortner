package com.aiassisted.urlshortener.repository;

import com.aiassisted.urlshortener.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    Optional<UrlMapping> findByToken(String token);
    Optional<UrlMapping> findByAlias(String alias);
    boolean existsByAlias(String alias);
    boolean existsByToken(String token);
}
