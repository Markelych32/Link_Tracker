package edu.java;

import edu.java.configuration.ApplicationConfig;
import edu.java.configuration.ClientConfig;
import edu.java.domain.chat.ChatDao;
import edu.java.domain.chat.JdbcChatDao;
import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.link.JdbcLinkDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import java.sql.Timestamp;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationConfig.class, ClientConfig.class})
public class ScrapperApplication {
    public static void main(String[] args) {
        var context = SpringApplication.run(ScrapperApplication.class, args);

        var underTest = context.getBean(JdbcLinkDao.class);

        System.out.println(underTest.findAll());
    }
}
