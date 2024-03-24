package edu.java.configuration;

import edu.java.domain.chat.JdbcChatDao;
import edu.java.domain.chat_link.JdbcChatLinkDao;
import edu.java.domain.link.JdbcLinkDao;
import edu.java.service.chat.ChatService;
import edu.java.service.chat.JdbcChatService;
import edu.java.service.link.JdbcLinkService;
import edu.java.service.link.LinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

    @Bean
    public LinkService linkService(
        JdbcLinkDao linkDao,
        JdbcChatDao chatDao,
        JdbcChatLinkDao chatLinkDao
    ) {
        return new JdbcLinkService(linkDao, chatDao, chatLinkDao);
    }

    @Bean
    public ChatService chatService(
        JdbcChatDao chatDao,
        JdbcChatLinkDao chatLinkDao
    ) {
        return new JdbcChatService(chatDao, chatLinkDao);
    }
}
