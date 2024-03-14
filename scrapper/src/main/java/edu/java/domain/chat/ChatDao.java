package edu.java.domain.chat;

import edu.java.domain.dto.Chat;
import java.util.List;

public interface ChatDao {
    Chat add(Long tgChatId);

    boolean remove(Long tgChatId);

    List<Chat> findAll();

}
