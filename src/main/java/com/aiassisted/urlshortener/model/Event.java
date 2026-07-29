package com.aiassisted.urlshortener.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id", nullable = false)
    private UrlMapping urlMapping;

    @Column(name = "occurred_at", nullable = false)
    private OffsetDateTime occurredAt = OffsetDateTime.now();

    @Column(name = "ip_hash")
    private String ipHash;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "referrer")
    private String referrer;

    @Column(name = "country")
    private String country;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public UrlMapping getUrlMapping() { return urlMapping; }
    public void setUrlMapping(UrlMapping urlMapping) { this.urlMapping = urlMapping; }
    public OffsetDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(OffsetDateTime occurredAt) { this.occurredAt = occurredAt; }
    public String getIpHash() { return ipHash; }
    public void setIpHash(String ipHash) { this.ipHash = ipHash; }
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    public String getReferrer() { return referrer; }
    public void setReferrer(String referrer) { this.referrer = referrer; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}
