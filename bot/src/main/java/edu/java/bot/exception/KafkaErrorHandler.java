package edu.java.bot.exception;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaErrorHandler implements CommonErrorHandler {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public boolean handleOne(
        @NotNull Exception thrownException,
        @NotNull ConsumerRecord<?, ?> consumerRecord,
        @NotNull Consumer<?, ?> consumer,
        @NotNull MessageListenerContainer container
    ) {
        log.error("Invalid link-update message");
        kafkaTemplate.send("link-updates-dlq", consumerRecord.value());
        return true;
    }
}
