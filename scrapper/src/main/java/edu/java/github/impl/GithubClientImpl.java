package edu.java.github.impl;

import edu.java.github.GithubClient;
import edu.java.github.RepositoryResponse;
import org.springframework.web.reactive.function.client.WebClient;

public class GithubClientImpl implements GithubClient {
    private final WebClient webClient;
    private static final String BASE_URL = "https://api.github.com";

    public GithubClientImpl(WebClient.Builder webClient) {
        this.webClient = webClient.baseUrl(BASE_URL).build();
    }

    public GithubClientImpl(WebClient.Builder webClient, String baseUrl) {
        this.webClient = webClient.baseUrl(baseUrl).build();
    }

    public GithubClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public RepositoryResponse fetchRepository(String owner, String repo) {
        return webClient.get()
            .uri("/repos/{owner}/{repo}", owner, repo)
            .retrieve()
            .bodyToMono(RepositoryResponse.class)
            .block();
    }
}
