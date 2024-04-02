package edu.java.bot.configuration.retryConfig;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "retry")
public record RetryConfig(
    RetryType retryType,
    int duration,
    int attempts,
    List<Integer> codes
) {
}

