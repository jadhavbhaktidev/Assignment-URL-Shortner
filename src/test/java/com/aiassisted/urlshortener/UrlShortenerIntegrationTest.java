package com.aiassisted.urlshortener;

import com.aiassisted.urlshortener.dto.ShortenRequest;
import com.aiassisted.urlshortener.dto.ShortenResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlShortenerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createAndRedirectShortUrl() {
        String url = "http://localhost:" + port + "/api/v1/shorten";
        ShortenRequest request = new ShortenRequest();
        request.setLongUrl("https://example.com");

        ResponseEntity<ShortenResponse> createResponse = restTemplate.postForEntity(url, request, ShortenResponse.class);
        assertThat(createResponse.getStatusCodeValue()).isEqualTo(201);
        assertThat(createResponse.getBody()).isNotNull();
        System.out.println("createResponse.id=" + createResponse.getBody().getId());
        System.out.println("createResponse.shortUrl=" + createResponse.getBody().getShortUrl());
        assertThat(createResponse.getBody().getShortUrl()).contains("http://localhost:");

        String shortUrl = createResponse.getBody().getShortUrl();
        ResponseEntity<String> redirectResponse = restTemplate.exchange(shortUrl, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);
        System.out.println("redirectResponse.status=" + redirectResponse.getStatusCodeValue());
        System.out.println("redirectResponse.body=" + redirectResponse.getBody());
        System.out.println("redirectResponse.location=" + redirectResponse.getHeaders().getLocation());
        assertThat(redirectResponse.getStatusCodeValue()).isEqualTo(302);
        assertThat(redirectResponse.getHeaders().getLocation().toString()).isEqualTo("https://example.com");
    }
}
