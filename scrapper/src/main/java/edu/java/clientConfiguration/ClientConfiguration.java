package edu.java.clientConfiguration;

import edu.java.github.impl.GithubClientImpl;
import edu.java.stackOverflow.impl.StackOverflowClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {
    private final WebClient.Builder webClientBuilder;

    @Bean
    public GithubClientImpl githubClient() {
        return new GithubClientImpl(webClientBuilder);
    }

    @Bean
    public StackOverflowClientImpl stackOverflowClient() {
        return new StackOverflowClientImpl(webClientBuilder);
    }
}
