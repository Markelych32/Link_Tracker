package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public record ApplicationConfig(
    @NotNull
    Kafka kafka,
    @NotNull
    Scheduler scheduler
) {
    public record Scheduler(boolean enable,
                            @NotNull Duration interval,
                            @NotNull int seconds,
                            @NotNull Duration forceCheckDelay) {
    }

    public record Kafka(@NotNull String bootstrapServer,
                        @NotNull String topicName,
                        @NotNull int partitions,
                        @NotNull short replications,
                        @NotNull String clientId,
                        @NotNull String acksMode,
                        @NotNull Duration deliveryTimeout,
                        @NotNull Integer lingerMs,
                        @NotNull Integer batchSize,
                        @NotNull Integer maxInFlightPerConnection,
                        @NotNull Boolean enableIdempotence

    ) {

    }
}
