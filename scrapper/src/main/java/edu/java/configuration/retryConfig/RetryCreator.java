package edu.java.configuration.retryConfig;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

@Configuration
@RequiredArgsConstructor
public class RetryCreator {

    private final RetryConfig retryConfig;

    @Bean
    public Retry createRetry() {
        switch (retryConfig.retryType()) {
            case CONSTANT -> {
                return Retry.fixedDelay(retryConfig.attempts(), Duration.ofSeconds(retryConfig.duration()))
                    .filter(this::isFiltered);
            }
            case EXPONENTIAL -> {
                return Retry.backoff(retryConfig.attempts(), Duration.ofSeconds(retryConfig.duration()))
                    .filter(this::isFiltered);
            }
            default -> {
                return new LinearRetry(retryConfig.duration(), retryConfig.attempts())
                    .filter(this::isFiltered);
            }
        }
    }

    private boolean isFiltered(Throwable throwable) {
        return throwable instanceof WebClientResponseException
               && retryConfig.codes().contains(((WebClientResponseException) throwable).getStatusCode().value());
    }
}
