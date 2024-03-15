package edu.java.service.chat;

import edu.java.domain.dto.Link;
import java.util.List;

public interface ChatService {
    void registerChat(Long tgChatId);

    void deleteChat(Long tgChatId);

    List<Long> findChatsByLink(Link link);

}
