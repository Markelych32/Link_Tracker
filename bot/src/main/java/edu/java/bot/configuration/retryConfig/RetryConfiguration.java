package edu.java.bot.configuration.retryConfig;

import edu.java.bot.configuration.ApplicationConfig;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

@Configuration
public class RetryConfiguration {

    private static final int DURATION = 5;
    private static final int ATTEMPTS = 2;

    @Bean
    public Retry createRetry(ApplicationConfig applicationConfig) {
        switch (applicationConfig.retryType()) {
            case CONSTANT -> {
                return Retry.fixedDelay(ATTEMPTS, Duration.ofSeconds(DURATION))
                    .filter(this::is5xxServerError);
            }
            case EXPONENTIAL -> {
                return Retry.backoff(ATTEMPTS, Duration.ofSeconds(DURATION))
                    .filter(this::is5xxServerError);
            }
            default -> {
                return new LinearRetry(DURATION, ATTEMPTS)
                    .filter(this::is5xxServerError);
            }
        }
    }

    private boolean is5xxServerError(Throwable throwable) {
        return throwable instanceof WebClientResponseException
               && ((WebClientResponseException) throwable).getStatusCode().is5xxServerError();
    }
}
