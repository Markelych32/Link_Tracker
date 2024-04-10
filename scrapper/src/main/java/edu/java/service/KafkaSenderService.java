package edu.java.service;

import edu.java.client.BotClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.controller.dto.response.LinkUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaSenderService {
    private final ScrapperQueueProducer queueProducer;
    private final BotClient botClient;
    private final ApplicationConfig applicationConfig;

    public void send(LinkUpdate linkUpdate) {
        if (applicationConfig.useQueue()) {
            queueProducer.send(linkUpdate);
        } else {
            botClient.updateLink(linkUpdate);
        }
    }
}
