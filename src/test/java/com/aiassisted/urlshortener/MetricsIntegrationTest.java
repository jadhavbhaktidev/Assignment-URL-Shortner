package com.aiassisted.urlshortener;

import com.aiassisted.urlshortener.dto.ShortenRequest;
import com.aiassisted.urlshortener.dto.ShortenResponse;
import com.aiassisted.urlshortener.dto.MetricsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MetricsIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void metricsEndpointRequiresApiKey() {
        String createUrl = "http://localhost:" + port + "/api/v1/shorten";
        ShortenRequest request = new ShortenRequest();
        request.setLongUrl("https://example.org");

        ResponseEntity<ShortenResponse> createResponse = restTemplate.postForEntity(createUrl, request, ShortenResponse.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();

        String metricsUrl = "http://localhost:" + port + "/api/v1/urls/" + createResponse.getBody().getId() + "/metrics";

        ResponseEntity<String> unauthorizedResponse = restTemplate.getForEntity(metricsUrl, String.class);
        assertThat(unauthorizedResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        String redirectUrl = "http://localhost:" + port + "/" + createResponse.getBody().getAlias();
        ResponseEntity<String> redirectResponse = restTemplate.exchange(
                redirectUrl,
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                String.class
        );
        assertThat(redirectResponse.getStatusCode()).isEqualTo(HttpStatus.FOUND);

        ResponseEntity<String> secondRedirectResponse = restTemplate.exchange(
            redirectUrl,
            HttpMethod.GET,
            new HttpEntity<>(new HttpHeaders()),
            String.class
        );
        assertThat(secondRedirectResponse.getStatusCode()).isEqualTo(HttpStatus.FOUND);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", "change-me");
        ResponseEntity<MetricsResponse> metricsResponse = restTemplate.exchange(metricsUrl, HttpMethod.GET, new HttpEntity<>(headers), MetricsResponse.class);
        assertThat(metricsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(metricsResponse.getBody()).isNotNull();
        assertThat(metricsResponse.getBody().getUrlId()).isEqualTo(createResponse.getBody().getId());
        assertThat(metricsResponse.getBody().getClicks()).isEqualTo(2L);
        assertThat(metricsResponse.getBody().getUniques()).isEqualTo(1L);
    }

    @Test
    void deleteEndpointRequiresApiKeyAndDeletesUrl() {
        String createUrl = "http://localhost:" + port + "/api/v1/shorten";
        ShortenRequest request = new ShortenRequest();
        request.setLongUrl("https://example-delete.org");

        ResponseEntity<ShortenResponse> createResponse = restTemplate.postForEntity(createUrl, request, ShortenResponse.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();

        String deleteUrl = "http://localhost:" + port + "/api/v1/urls/" + createResponse.getBody().getId();

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(deleteUrl).openConnection();
            conn.setRequestMethod("DELETE");
            conn.connect();
            assertThat(conn.getResponseCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            conn.disconnect();
        } catch (Exception ex) {
            throw new AssertionError("Unauthorized DELETE request failed", ex);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", "change-me");
        ResponseEntity<String> authorizedDelete = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
        assertThat(authorizedDelete.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> secondDelete = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
        assertThat(secondDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
