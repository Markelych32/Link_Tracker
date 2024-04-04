package edu.java.configuration.retryConfig;

import java.time.Duration;
import java.util.function.Predicate;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class LinearRetry extends Retry {

    private final int duration;
    private final int attempts;
    private Predicate<? super Throwable> filter;

    public LinearRetry(int duration, int attempts) {
        this.duration = duration;
        this.attempts = attempts;
    }

    @Override
    public Publisher<?> generateCompanion(Flux<RetrySignal> flux) {
        return flux.flatMap(this::generateRetry);
    }

    public LinearRetry filter(Predicate<? super Throwable> errorFilter) {
        this.filter = errorFilter;
        return this;
    }

    private Mono<Long> generateRetry(RetrySignal rs) {
        if (!this.filter.test(rs.failure())) {
            return Mono.error(rs.failure());
        }

        if (rs.totalRetries() < attempts) {
            Duration newDelay = Duration.ofSeconds(duration).multipliedBy(rs.totalRetries());
            return Mono.delay(newDelay).thenReturn(rs.totalRetries());
        } else {
            return Mono.error(rs.failure());
        }
    }
}
