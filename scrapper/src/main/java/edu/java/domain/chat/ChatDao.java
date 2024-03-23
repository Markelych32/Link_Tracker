package edu.java.domain.chat;

import edu.java.domain.dto.jdbc.Chat;
import java.util.List;
import java.util.Optional;

public interface ChatDao {
    boolean add(Long tgChatId);

    boolean remove(Long tgChatId);

    List<Chat> findAll();

    Optional<Chat> find(Long tgChatId);

}
