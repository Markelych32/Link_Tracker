package edu.java.bot.client;

import edu.java.bot.controller.dto.LinkUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class BotClient {

    private static final String BASE_URL = "http://localhost:8090";
    private static final String BOT_API_V_1_0_UPDATES = "bot-api/v1.0/updates";

    private final WebClient webClient;

    @Autowired
    public BotClient(WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public BotClient(WebClient.Builder webClientBuilder, String base_url) {
        webClient = webClientBuilder.baseUrl(base_url).build();
    }

    public void updateLink(LinkUpdate linkUpdate) {
        webClient.post()
            .uri(BOT_API_V_1_0_UPDATES)
            .bodyValue(linkUpdate)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}
