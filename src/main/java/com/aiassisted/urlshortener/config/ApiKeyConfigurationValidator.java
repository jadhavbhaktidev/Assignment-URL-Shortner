package com.aiassisted.urlshortener.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ApiKeyConfigurationValidator implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyConfigurationValidator.class);
    private static final String INSECURE_DEFAULT_KEY = "change-me";

    private final String apiKey;
    private final Environment environment;

    public ApiKeyConfigurationValidator(@Value("${urlshortener.api-key}") String apiKey, Environment environment) {
        this.apiKey = apiKey;
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) {
        boolean isProd = Arrays.asList(environment.getActiveProfiles()).contains("prod");
        if (INSECURE_DEFAULT_KEY.equals(apiKey)) {
            String message = "Default API key 'change-me' is active. Set URLSHORTENER_API_KEY for secure deployments.";
            if (isProd) {
                throw new IllegalStateException(message);
            }
            logger.warn(message);
        }
    }
}