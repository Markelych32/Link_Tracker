package edu.java.stackOverflow;

import java.util.Objects;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClient {

    private static final String BASE_URL = "https://api.stackexchange.com";
    private final WebClient webClient;

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
