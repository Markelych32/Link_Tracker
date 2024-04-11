package edu.java.bot.service;

import edu.java.bot.controller.dto.request.LinkUpdate;
import edu.java.bot.processor.BotMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaListenerService {

    private final BotMessageSender botMessageSender;

    @KafkaListener(topics = "${app.scrapper-topic-name}",
                   containerFactory = "linkUpdateContainerFactory")
    public void listen(@Payload LinkUpdate update, Acknowledgment acknowledgment) {
        log.info("Link update info received: " + update);
        botMessageSender.sendMessage(
            update.getTgChatIds(),
            update.getDescription()
        );
        acknowledgment.acknowledge();
    }
}
