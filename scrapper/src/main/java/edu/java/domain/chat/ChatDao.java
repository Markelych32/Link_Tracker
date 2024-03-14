package edu.java.domain.chat;

import edu.java.domain.dto.Chat;
import java.util.List;

public interface ChatDao {
    boolean add(Chat chat);

    boolean remove(Chat chat);

    List<Chat> findAll();

}
