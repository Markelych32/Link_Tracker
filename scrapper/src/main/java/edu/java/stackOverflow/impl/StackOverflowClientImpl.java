package edu.java.stackOverflow.impl;

import edu.java.stackOverflow.ItemResponse;
import edu.java.stackOverflow.QuestionResponse;
import edu.java.stackOverflow.StackOverflowClient;
import java.util.Objects;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClientImpl implements StackOverflowClient {

    private final static String BASE_URL = "https://api.stackexchange.com";
    private final WebClient webClient;

    public StackOverflowClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
            .baseUrl(BASE_URL)
            .build();
    }

    public StackOverflowClientImpl(WebClient.Builder webClientBuilder, String url) {
        this.webClient = webClientBuilder
            .baseUrl(url)
            .build();
    }

    @Override
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
