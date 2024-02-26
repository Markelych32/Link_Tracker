package edu.java.service;

import edu.java.exception.ChatAlreadyExistException;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinksAlreadyRegisteredInChatException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScrapperService {
    private final Map<Long, List<String>> links = new HashMap<>();

    public void saveChatById(Long tgChatId) {
        if (links.containsKey(tgChatId)) {
            throw new ChatAlreadyExistException();
        }
        links.put(tgChatId, new ArrayList<>());
    }

    public void deleteTgChatById(Long tgChatId) {
        if (!links.containsKey(tgChatId)) {
            throw new ChatNotFoundException();
        }
        links.remove(tgChatId);
    }

    public List<String> addLinkByChatId(Long tgChatId, String url) {
        if (!links.containsKey(tgChatId)) {
            throw new ChatNotFoundException();
        }
        if (links.get(tgChatId).contains(url)) {
            throw new LinksAlreadyRegisteredInChatException();
        }
        return links.get(tgChatId);
    }

}
