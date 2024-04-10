package edu.java.configuration.kafka;

import edu.java.configuration.ApplicationConfig;
import edu.java.controller.dto.response.LinkUpdate;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

    private final ApplicationConfig applicationConfig;

    public NewTopic linkUpdate() {
        return new NewTopic(
            applicationConfig.kafka().topicName(),
            applicationConfig.kafka().partitions(),
            applicationConfig.kafka().replications()
        );
    }

    @Bean
    public ProducerFactory<String, LinkUpdate> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, applicationConfig.kafka().bootstrapServer());
        configProps.put(ProducerConfig.CLIENT_ID_CONFIG, applicationConfig.kafka().clientId());
        configProps.put(ProducerConfig.ACKS_CONFIG, applicationConfig.kafka().acksMode());
        configProps.put(
            ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,
            applicationConfig.kafka().deliveryTimeout().toMillis()
        );
        configProps.put(
            ProducerConfig.LINGER_MS_CONFIG,
            applicationConfig.kafka().lingerMs()
        );
        configProps.put(
            ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,
            applicationConfig.kafka().maxInFlightPerConnection()
        );
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, applicationConfig.kafka().enableIdempotence());
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, LinkUpdate> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
