package edu.java.configuration;

import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import edu.java.service.chat.ChatService;
import edu.java.service.chat.JpaChatService;
import edu.java.service.link.JpaLinkService;
import edu.java.service.link.LinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {

    @Bean
    public LinkService linkService(
        LinkRepository linkRepository,
        ChatRepository chatRepository
    ) {
        return new JpaLinkService(linkRepository, chatRepository);
    }

    @Bean
    public ChatService chatService(
        ChatRepository chatRepository,
        LinkRepository linkRepository
    ) {
        return new JpaChatService(chatRepository, linkRepository);
    }
}
