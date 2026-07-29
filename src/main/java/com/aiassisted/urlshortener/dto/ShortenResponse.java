package com.aiassisted.urlshortener.dto;

public class ShortenResponse {
    private Long id;
    private String shortUrl;
    private String alias;

    public ShortenResponse(Long id, String shortUrl, String alias) {
        this.id = id;
        this.shortUrl = shortUrl;
        this.alias = alias;
    }

    public Long getId() { return id; }
    public String getShortUrl() { return shortUrl; }
    public String getAlias() { return alias; }
}
