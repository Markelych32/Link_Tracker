package edu.java.clientConfiguration;

import edu.java.github.GithubClient;
import edu.java.stackOverflow.StackOverflowClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {
    private final WebClient.Builder webClientBuilder;

    @Bean
    public GithubClient githubClient() {
        return new GithubClient(webClientBuilder);
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClient(webClientBuilder);
    }
}
