package com.aiassisted.urlshortener;

import com.aiassisted.urlshortener.dto.ShortenRequest;
import com.aiassisted.urlshortener.dto.ShortenResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

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
        assertThat(createResponse.getBody().getShortUrl()).contains("http://localhost:");

        String shortUrl = createResponse.getBody().getShortUrl();
        ResponseEntity<String> redirectResponse = restTemplate.exchange(shortUrl, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);
        assertThat(redirectResponse.getStatusCodeValue()).isEqualTo(302);
        assertThat(redirectResponse.getHeaders().getLocation().toString()).isEqualTo("https://example.com");
    }

    @Test
    void duplicateAliasReturnsConflict() {
        String url = "http://localhost:" + port + "/api/v1/shorten";

        ShortenRequest first = new ShortenRequest();
        first.setLongUrl("https://example.com/1");
        first.setCustomAlias("alias1234");

        ShortenRequest second = new ShortenRequest();
        second.setLongUrl("https://example.com/2");
        second.setCustomAlias("alias1234");

        ResponseEntity<String> firstResponse = restTemplate.postForEntity(url, first, String.class);
        ResponseEntity<String> secondResponse = restTemplate.postForEntity(url, second, String.class);

        assertThat(firstResponse.getStatusCodeValue()).isEqualTo(201);
        assertThat(secondResponse.getStatusCodeValue()).isEqualTo(409);
    }

    @Test
    void redirectUnknownTokenReturnsNotFound() {
        String url = "http://localhost:" + port + "/does-not-exist";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }
}
