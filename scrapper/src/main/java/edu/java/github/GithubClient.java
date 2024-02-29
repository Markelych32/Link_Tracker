package edu.java.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GithubClient {
    private final WebClient webClient;
    private static final String BASE_URL = "https://api.github.com";

    @Autowired
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
