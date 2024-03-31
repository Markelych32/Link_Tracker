package edu.java.bot.configuration.retryConfig;

import edu.java.bot.configuration.ApplicationConfig;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

@Configuration
public class RetryConfiguration {

    private static final int BACKOFF = 2;
    private static final int MAX_ATTEMPTS = 3;

    @Bean
    public Retry createRetry(ApplicationConfig applicationConfig) {
        switch (applicationConfig.retryType()) {
            case CONSTANT -> {
                return Retry.fixedDelay(MAX_ATTEMPTS, Duration.ofSeconds(BACKOFF))
                    .filter(this::is5xxServerError);
            }
            case EXPONENTIAL -> {
                return Retry.backoff(MAX_ATTEMPTS, Duration.ofSeconds(BACKOFF))
                    .filter(this::is5xxServerError);
            }
            default -> {
                return null;
            }
        }
    }

    private boolean is5xxServerError(Throwable throwable) {
        return throwable instanceof WebClientResponseException
               && ((WebClientResponseException) throwable).getStatusCode().is5xxServerError();
    }
}