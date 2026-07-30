package com.aiassisted.urlshortener.repository;

import com.aiassisted.urlshortener.model.AggregateCount;
import com.aiassisted.urlshortener.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AggregateCountRepository extends JpaRepository<AggregateCount, Long> {
    Optional<AggregateCount> findByUrlMappingAndAggregateDay(UrlMapping urlMapping, LocalDate aggregateDay);
}
