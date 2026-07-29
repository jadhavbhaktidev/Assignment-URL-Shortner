package com.aiassisted.urlshortener.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "urls")
public class UrlMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(unique = true)
    private String alias;

    @Column(name = "long_url", nullable = false, columnDefinition = "TEXT")
    private String longUrl;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    @Column(name = "api_key_owner")
    private String apiKeyOwner;

    @Column(name = "clicks_count", nullable = false)
    private Long clicksCount = 0L;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
    public String getLongUrl() { return longUrl; }
    public void setLongUrl(String longUrl) { this.longUrl = longUrl; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(OffsetDateTime expiresAt) { this.expiresAt = expiresAt; }
    public String getApiKeyOwner() { return apiKeyOwner; }
    public void setApiKeyOwner(String apiKeyOwner) { this.apiKeyOwner = apiKeyOwner; }
    public Long getClicksCount() { return clicksCount; }
    public void setClicksCount(Long clicksCount) { this.clicksCount = clicksCount; }
}
