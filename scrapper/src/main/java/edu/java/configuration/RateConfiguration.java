package edu.java.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rate")
public record RateConfiguration(
    int limit,
    int refill
) {
}
