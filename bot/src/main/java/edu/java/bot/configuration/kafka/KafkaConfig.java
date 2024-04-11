package edu.java.bot.configuration.kafka;

import edu.java.bot.configuration.kafka.properties.KafkaConsumer;
import edu.java.bot.configuration.kafka.properties.KafkaProducer;
import edu.java.bot.controller.dto.request.LinkUpdate;
import edu.java.bot.exception.KafkaErrorHandler;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
@Slf4j
public class KafkaConfig {
    @Bean
    public ConsumerFactory<String, LinkUpdate> linkUpdateConsumerFactory(KafkaConsumer kafkaConsumer) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumer.getBootstrapServer());
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumer.getGroupId());
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumer.getAutoOffsetReset());
        configProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaConsumer.getMaxPollIntervalMs());
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaConsumer.getEnableAutoCommit());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, LinkUpdate.class);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LinkUpdate>
    linkUpdateContainerFactory(
        KafkaConsumer kafkaConsumer,
        KafkaErrorHandler kafkaErrorHandler,
        ConsumerFactory<String, LinkUpdate> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, LinkUpdate> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(kafkaErrorHandler);
        factory.setConcurrency(kafkaConsumer.getConcurrency());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory(KafkaProducer kafkaProducer) {
        Map<String, Object> props = new HashMap<>();
        props.put(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
            kafkaProducer.getBootstrapServer()
        );
        props.put(
            ProducerConfig.CLIENT_ID_CONFIG,
            kafkaProducer.getClientId()
        );
        props.put(
            ProducerConfig.ACKS_CONFIG,
            kafkaProducer.getAcksMode()
        );
        props.put(
            ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,
            kafkaProducer.getDeliveryTimeout().toMillis()
        );
        props.put(
            ProducerConfig.LINGER_MS_CONFIG,
            kafkaProducer.getLingerMs()
        );
        props.put(
            ProducerConfig.BATCH_SIZE_CONFIG,
            kafkaProducer.getBatchSize()
        );
        props.put(
            ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,
            kafkaProducer.getMaxInFlightPerConnection()
        );
        props.put(
            ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,
            kafkaProducer.getEnableIdempotence()
        );
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(
        KafkaProducer kafkaProducer
    ) {
        return new KafkaTemplate<>(producerFactory(kafkaProducer));
    }

}
