package edu.java.stackOverflow;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class StackOverflowClient {

    private static final String BASE_URL = "https://api.stackexchange.com";
    private final WebClient webClient;

    @Autowired
    public StackOverflowClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
            .baseUrl(BASE_URL)
            .build();
    }

    public StackOverflowClient(WebClient.Builder webClientBuilder, String url) {
        this.webClient = webClientBuilder
            .baseUrl(url)
            .build();
    }

    public ItemResponse fetchQuestion(String id) {
        return Objects.requireNonNull(webClient.get()
                .uri("/questions/{id}?site=stackoverflow", id)
                .retrieve()
                .bodyToMono(QuestionResponse.class)
                .block())
            .items()
            .getFirst();
    }
}
