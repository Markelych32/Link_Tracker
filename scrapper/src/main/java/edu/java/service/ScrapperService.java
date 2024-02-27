package edu.java.service;

import edu.java.controller.dto.AddLinkRequest;
import edu.java.controller.dto.LinkResponse;
import edu.java.controller.dto.ListLinksResponse;
import edu.java.exception.ChatAlreadyExistException;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkAlreadyRegisteredInChatException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScrapperService {
    private final Map<Long, List<LinkResponse>> links = new HashMap<>();

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

    public ListLinksResponse getLinksByChatId(Long tgChatId) {
        if (!links.containsKey(tgChatId)) {
            throw new ChatNotFoundException();
        }
        var result = links.get(tgChatId);
        return new ListLinksResponse(result, result.size());
    }

    public LinkResponse addLinkByChatId(Long tgChatId, AddLinkRequest addLinkRequest) {
        if (!links.containsKey(tgChatId)) {
            throw new ChatNotFoundException();
        }
        String urlLinkRequest = addLinkRequest.getUrl();
        List<LinkResponse> list = links.get(tgChatId);
        if (list.stream().anyMatch(linkResponse -> linkResponse.getUrl().equals(urlLinkRequest))) {
            throw new LinkAlreadyRegisteredInChatException();
        }
        var result = new LinkResponse(++LinkResponse.COUNTER, urlLinkRequest);
        links.get(tgChatId).add(result);
        return result;
    }

}
