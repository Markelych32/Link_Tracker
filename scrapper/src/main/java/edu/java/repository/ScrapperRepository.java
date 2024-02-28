package edu.java.repository;

import edu.java.controller.dto.AddLinkRequest;
import edu.java.controller.dto.LinkResponse;
import edu.java.controller.dto.ListLinksResponse;
import edu.java.controller.dto.RemoveLinkRequest;
import edu.java.exception.ChatAlreadyExistException;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkAlreadyRegisteredInChatException;
import edu.java.exception.LinkNotFoundByUrlException;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ScrapperRepository {
    private final Map<Long, List<LinkResponse>> links = new HashMap<>();

    public void saveByChatId(Long tgChatId) {
        if (links.containsKey(tgChatId)) {
            throw new ChatAlreadyExistException();
        }
        links.put(tgChatId, new ArrayList<>());
    }

    public void deleteChatByChatId(Long tgChatId) {
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
        var result = new LinkResponse(++LinkResponse.counter, urlLinkRequest);
        links.get(tgChatId).add(result);
        return result;
    }

    public LinkResponse deleteLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        if (!links.containsKey(tgChatId)) {
            throw new ChatNotFoundException();
        }
        List<LinkResponse> list = links.get(tgChatId);
        String url = removeLinkRequest.getUrl();
        if (list.stream().noneMatch(linkResponse -> linkResponse.getUrl().equals(url))) {
            throw new LinkNotFoundByUrlException();
        }
        LinkResponse result = list.stream().filter(l -> l.getUrl().equals(url)).findFirst().get();
        list.remove(result);
        return result;
    }
}
