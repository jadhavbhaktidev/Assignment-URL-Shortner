package com.aiassisted.urlshortener.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "aggregates", uniqueConstraints = @UniqueConstraint(columnNames = {"url_id", "aggregate_day"}))
public class AggregateCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id", nullable = false)
    private UrlMapping urlMapping;

    @Column(name = "aggregate_day", nullable = false)
    private LocalDate aggregateDay;

    @Column(nullable = false)
    private Long clicks = 0L;

    @Column(nullable = false)
    private Long uniques = 0L;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public UrlMapping getUrlMapping() { return urlMapping; }
    public void setUrlMapping(UrlMapping urlMapping) { this.urlMapping = urlMapping; }
    public LocalDate getAggregateDay() { return aggregateDay; }
    public void setAggregateDay(LocalDate aggregateDay) { this.aggregateDay = aggregateDay; }
    public Long getClicks() { return clicks; }
    public void setClicks(Long clicks) { this.clicks = clicks; }
    public Long getUniques() { return uniques; }
    public void setUniques(Long uniques) { this.uniques = uniques; }
}
