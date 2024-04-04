package edu.java;

import edu.java.configuration.ApplicationConfig;
import edu.java.configuration.ClientConfig;
import edu.java.configuration.retryConfig.RetryConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationConfig.class, ClientConfig.class, RetryConfig.class})
public class ScrapperApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }
}
