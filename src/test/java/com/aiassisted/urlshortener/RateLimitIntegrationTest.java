package com.aiassisted.urlshortener;

import com.aiassisted.urlshortener.dto.ShortenRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "urlshortener.rate-limit.enabled=true",
                "urlshortener.rate-limit.max-requests=2",
                "urlshortener.rate-limit.window-seconds=60"
        }
)
public class RateLimitIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void thirdPublicRequestWithinWindowReturnsTooManyRequests() {
        String url = "http://localhost:" + port + "/api/v1/shorten";

        ShortenRequest first = new ShortenRequest();
        first.setLongUrl("https://ratelimit-example-1.org");
        ShortenRequest second = new ShortenRequest();
        second.setLongUrl("https://ratelimit-example-2.org");
        ShortenRequest third = new ShortenRequest();
        third.setLongUrl("https://ratelimit-example-3.org");

        ResponseEntity<String> firstResponse = restTemplate.postForEntity(url, first, String.class);
        ResponseEntity<String> secondResponse = restTemplate.postForEntity(url, second, String.class);
        ResponseEntity<String> thirdResponse = restTemplate.postForEntity(url, third, String.class);

        assertThat(firstResponse.getStatusCodeValue()).isEqualTo(201);
        assertThat(secondResponse.getStatusCodeValue()).isEqualTo(201);
        assertThat(thirdResponse.getStatusCodeValue()).isEqualTo(429);
        assertThat(thirdResponse.getBody()).contains("RATE_LIMITED");
    }
}