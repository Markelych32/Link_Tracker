package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.controller.MainBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(BotApplication.class, args);
        MainBot bot = applicationContext.getBean(MainBot.class);
        bot.start();
    }
}
