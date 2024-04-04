package edu.java.client;

import edu.java.configuration.ClientConfig;
import edu.java.controller.dto.response.LinkUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

@Component
public class BotClient {

    private static final String BOT_API_V_1_0_UPDATES = "/updates";

    private final WebClient webClient;
    private final ClientConfig clientConfig;
    private final Retry retry;

    @Autowired
    public BotClient(WebClient.Builder webClientBuilder, ClientConfig clientConfig, Retry retry) {
        webClient = webClientBuilder.baseUrl(clientConfig.baseUrl()).build();
        this.clientConfig = clientConfig;
        this.retry = retry;
    }

    public BotClient(WebClient.Builder webClientBuilder, ClientConfig clientConfig, String baseUrl, Retry retry) {
        webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.clientConfig = clientConfig;
        this.retry = retry;
    }

    public void updateLink(LinkUpdate linkUpdate) {
        webClient.post()
            .uri(BOT_API_V_1_0_UPDATES)
            .bodyValue(linkUpdate)
            .retrieve()
            .bodyToMono(LinkUpdate.class)
            .retryWhen(retry)
            .block();
    }
}

