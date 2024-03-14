package edu.java;

import edu.java.configuration.ApplicationConfig;
import edu.java.configuration.ClientConfig;
import edu.java.domain.chat.JdbcChatDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationConfig.class, ClientConfig.class})
public class ScrapperApplication {
    public static void main(String[] args) {
        var context = SpringApplication.run(ScrapperApplication.class, args);

        var underTest = context.getBean(JdbcChatDao.class);

        System.out.println(underTest.findAll());
        System.out.println(underTest.remove(3046293L));
        System.out.println(underTest.findAll());
        System.out.println(underTest.add(92348526L));
        System.out.println(underTest.findAll());
    }
}
