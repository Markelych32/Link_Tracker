package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.controller.MainBot;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
@RequiredArgsConstructor
public class BotApplication {

    private final MainBot mainBot;

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }
}
