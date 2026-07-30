package com.aiassisted.urlshortener.dto;

public class MetricsResponse {
    private Long urlId;
    private Long clicks;
    private Long uniques;

    public MetricsResponse() {
    }

    public MetricsResponse(Long urlId, Long clicks, Long uniques) {
        this.urlId = urlId;
        this.clicks = clicks;
        this.uniques = uniques;
    }

    public Long getUrlId() { return urlId; }
    public void setUrlId(Long urlId) { this.urlId = urlId; }
    public Long getClicks() { return clicks; }
    public void setClicks(Long clicks) { this.clicks = clicks; }
    public Long getUniques() { return uniques; }
    public void setUniques(Long uniques) { this.uniques = uniques; }
}
