package com.aiassisted.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ShortenRequest {

    @NotBlank
    private String longUrl;

    @Pattern(regexp = "^[A-Za-z0-9_-]{4,100}$", message = "Alias must be 4-100 chars and contain only letters, digits, underscore, or hyphen")
    private String customAlias;

    public String getLongUrl() { return longUrl; }
    public void setLongUrl(String longUrl) { this.longUrl = longUrl; }
    public String getCustomAlias() { return customAlias; }
    public void setCustomAlias(String customAlias) { this.customAlias = customAlias; }
}
