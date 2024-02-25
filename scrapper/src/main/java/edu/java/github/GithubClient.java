package edu.java.github;

import org.springframework.web.reactive.function.client.WebClient;

public class GithubClient {
    private final WebClient webClient;
    private static final String BASE_URL = "https://api.github.com";

    public GithubClient(WebClient.Builder webClient) {
        this.webClient = webClient.baseUrl(BASE_URL).build();
    }

    public GithubClient(WebClient.Builder webClient, String baseUrl) {
        this.webClient = webClient.baseUrl(baseUrl).build();
    }

    public GithubClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public RepositoryResponse fetchRepository(String owner, String repo) {
        return webClient.get()
            .uri("/repos/{owner}/{repo}", owner, repo)
            .retrieve()
            .bodyToMono(RepositoryResponse.class)
            .block();
    }
}
