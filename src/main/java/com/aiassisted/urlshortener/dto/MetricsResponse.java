package com.aiassisted.urlshortener.dto;

public class MetricsResponse {
    private Long urlId;
    private Long clicks;
    private Long uniques;

    public MetricsResponse(Long urlId, Long clicks, Long uniques) {
        this.urlId = urlId;
        this.clicks = clicks;
        this.uniques = uniques;
    }

    public Long getUrlId() { return urlId; }
    public Long getClicks() { return clicks; }
    public Long getUniques() { return uniques; }
}
