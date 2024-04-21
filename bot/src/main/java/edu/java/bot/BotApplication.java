package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.ClientConfig;
import edu.java.bot.configuration.kafka.properties.KafkaConsumer;
import edu.java.bot.configuration.kafka.properties.KafkaProducer;
import edu.java.bot.configuration.retryConfig.RetryConfig;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

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

    @Bean
    MeterBinder meterBinder() {
        return registry -> {
            Counter.builder("messages_count")
                .description("Total messages received")
                .register(registry);
        };
    }
}

