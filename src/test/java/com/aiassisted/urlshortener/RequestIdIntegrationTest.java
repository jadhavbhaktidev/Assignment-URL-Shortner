package com.aiassisted.urlshortener;

import com.aiassisted.urlshortener.dto.ShortenRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class RequestIdIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void generatedRequestIdIsReturnedWhenHeaderIsMissing() {
        String url = "http://localhost:" + port + "/api/v1/shorten";
        ShortenRequest request = new ShortenRequest();
        request.setLongUrl("https://example-request-id-1.org");

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getHeaders().getFirst("X-Request-ID")).isNotBlank();
    }

    @Test
    void incomingRequestIdIsEchoedBackInResponseHeader() {
        String url = "http://localhost:" + port + "/api/v1/shorten";
        ShortenRequest request = new ShortenRequest();
        request.setLongUrl("https://example-request-id-2.org");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-ID", "test-request-id-123");
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getHeaders().getFirst("X-Request-ID")).isEqualTo("test-request-id-123");
    }

    @Test
    void validationErrorResponseContainsGeneratedRequestId() throws Exception {
        String url = "http://localhost:" + port + "/api/v1/shorten";
        ShortenRequest request = new ShortenRequest();
        request.setLongUrl(" ");

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        String requestIdHeader = response.getHeaders().getFirst("X-Request-ID");
        assertThat(requestIdHeader).isNotBlank();

        JsonNode body = objectMapper.readTree(response.getBody());
        assertThat(body.path("requestId").asText()).isEqualTo(requestIdHeader);
    }
}