package edu.java.bot.configuration.kafka.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "kafka.consumer")
public class KafkaConsumer {
    private String bootstrapServer;
    private String groupId;
    private String autoOffsetReset;
    private Integer maxPollIntervalMs;
    private Boolean enableAutoCommit;
    private Integer concurrency;
}
