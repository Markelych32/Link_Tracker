package edu.java;

import edu.java.configuration.ApplicationConfig;
import edu.java.github.impl.GithubClientImpl;
import edu.java.stackOverflow.impl.StackOverflowClientImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
@Slf4j
public class ScrapperApplication {
    public static void main(String[] args) {
        var context = SpringApplication.run(ScrapperApplication.class, args);

        GithubClientImpl githubClient = context.getBean(GithubClientImpl.class);
        StackOverflowClientImpl stackOverflowClient = context.getBean(StackOverflowClientImpl.class);

        log.info(githubClient.fetchRepository("Markelych32", "Book_REST_API").toString());
        log.info(stackOverflowClient.fetchQuestion("21007680").toString());
    }
}
