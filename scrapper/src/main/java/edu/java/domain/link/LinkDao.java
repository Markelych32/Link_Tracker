package edu.java.domain.link;

import edu.java.domain.dto.Link;
import java.util.List;

public interface LinkDao {
    boolean add(Long chatId, Link link);
    boolean remove(Long tgChatId, String url);

    List<Link> findAll();
}
