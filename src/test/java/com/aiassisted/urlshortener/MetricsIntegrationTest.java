package com.aiassisted.urlshortener;

import com.aiassisted.urlshortener.dto.ShortenRequest;
import com.aiassisted.urlshortener.dto.ShortenResponse;
import com.aiassisted.urlshortener.dto.MetricsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

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

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", "change-me");
        ResponseEntity<MetricsResponse> metricsResponse = restTemplate.exchange(metricsUrl, HttpMethod.GET, new HttpEntity<>(headers), MetricsResponse.class);
        assertThat(metricsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(metricsResponse.getBody()).isNotNull();
        assertThat(metricsResponse.getBody().getUrlId()).isEqualTo(createResponse.getBody().getId());
    }
}
