package edu.java.repository;

import edu.java.controller.dto.AddLinkRequest;
import edu.java.controller.dto.LinkResponse;
import edu.java.controller.dto.RemoveLinkRequest;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ScrapperRepository {
    private final Map<Long, List<LinkResponse>> links = new HashMap<>();

    public boolean isChatAlreadyExist(Long tgChatId) {
        return links.containsKey(tgChatId);
    }

    public void saveByChatId(Long tgChatId) {
        links.put(tgChatId, new ArrayList<>());
    }

    public void deleteChatByChatId(Long tgChatId) {
        links.remove(tgChatId);
    }

    public List<LinkResponse> getLinksByChatId(Long tgChatId) {
        return links.get(tgChatId);
    }

    public LinkResponse addLinkByChatId(Long tgChatId, AddLinkRequest addLinkRequest) {
        var result = new LinkResponse(++LinkResponse.counter, addLinkRequest.getUrl());
        links.get(tgChatId).add(result);
        return result;
    }

    public LinkResponse deleteLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        List<LinkResponse> list = links.get(tgChatId);
        String url = removeLinkRequest.getUrl();
        LinkResponse result = list.stream().filter(l -> l.getUrl().equals(url)).findFirst().get();
        list.remove(result);
        return result;
    }
}
