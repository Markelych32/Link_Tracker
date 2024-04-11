package edu.java.bot.configuration.kafka.properties;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "kafka.producer")
public class KafkaProducer {
    private String bootstrapServer;
    private String clientId;
    private String acksMode;
    private Duration deliveryTimeout;
    private Integer lingerMs;
    private Integer batchSize;
    private Integer maxInFlightPerConnection;
    private Boolean enableIdempotence;
}
