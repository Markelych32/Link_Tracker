package edu.java.domain.chat_link;

import edu.java.domain.dto.jdbc.ChatLink;
import edu.java.domain.dto.jdbc.Link;
import java.util.List;
import java.util.Optional;

public interface ChatLinkDao {
    boolean add(Long chatId, Long linkId);

    boolean remove(Long chatId, Link link);

    List<ChatLink> findAll();

    Optional<ChatLink> find(Long chatId, Long linkId);

    List<Long> findChatsByLink(Link link);

    boolean isLinkPresent(Link link);
}
