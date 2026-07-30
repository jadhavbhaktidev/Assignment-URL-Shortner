package com.aiassisted.urlshortener.repository;

import com.aiassisted.urlshortener.model.Event;
import com.aiassisted.urlshortener.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
	@Query("select count(distinct e.ipHash) from Event e where e.urlMapping.id = :urlMappingId and e.ipHash is not null")
	long countDistinctIpHashByUrlMappingId(@Param("urlMappingId") Long urlMappingId);

	boolean existsByUrlMappingAndIpHashAndOccurredAtBetween(UrlMapping urlMapping, String ipHash, OffsetDateTime start, OffsetDateTime end);
}
