package edu.java.service;

import edu.java.controller.dto.response.LinkUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrapperQueueProducer {
    private final KafkaTemplate<String, LinkUpdate> kafkaTemplate;

    public void send(LinkUpdate linkUpdate) {
        try {
            kafkaTemplate.send("link-update", linkUpdate);
        } catch (Exception exception) {
            log.error("Error occurred during sending to Kafka", exception);
        }
    }
}
