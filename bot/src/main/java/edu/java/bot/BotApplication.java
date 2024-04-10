package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.ClientConfig;
import edu.java.bot.configuration.kafka.properties.KafkaConsumer;
import edu.java.bot.configuration.kafka.properties.KafkaProducer;
import edu.java.bot.configuration.retryConfig.RetryConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    ApplicationConfig.class,
    ClientConfig.class,
    RetryConfig.class,
    KafkaConsumer.class,
    KafkaProducer.class
})
@RequiredArgsConstructor
public class BotApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }
}
