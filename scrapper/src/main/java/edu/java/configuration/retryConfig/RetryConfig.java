package edu.java.configuration.retryConfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@ConfigurationProperties(prefix = "retry")
public record RetryConfig(
    RetryType retryType,
    int duration,
    int attempts,
    List<Integer> codes
) {
}
