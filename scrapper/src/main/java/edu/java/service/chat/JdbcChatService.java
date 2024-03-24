package edu.java.service.chat;

import edu.java.domain.chat.JdbcChatDao;
import edu.java.domain.chat_link.JdbcChatLinkDao;
import edu.java.domain.dto.jdbc.Link;
import edu.java.exception.ChatAlreadyExistException;
import edu.java.exception.ChatNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcChatService implements ChatService {

    private final JdbcChatDao chatDao;
    private final JdbcChatLinkDao chatLinkDao;

    @Override
    public void registerChat(Long tgChatId) {
        if (chatDao.find(tgChatId).isPresent()) {
            throw new ChatAlreadyExistException();
        }
        chatDao.add(tgChatId);
    }

    @Override
    public void deleteChat(Long tgChatId) {
        if (chatDao.find(tgChatId).isEmpty()) {
            throw new ChatNotFoundException();
        }
        chatDao.remove(tgChatId);
    }

    @Override
    public List<Long> findChatsByLink(Link link) {
        return chatLinkDao.findChatsByLink(link);
    }
}
