package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.utils.Bot;
import edu.java.bot.utils.MainBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(BotApplication.class, args);
        Bot bot = applicationContext.getBean(MainBot.class);
        bot.start();
    }
}
