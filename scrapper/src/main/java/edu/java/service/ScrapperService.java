package edu.java.service;

import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.controller.dto.response.LinkResponse;
import edu.java.controller.dto.response.ListLinksResponse;
import edu.java.exception.ChatAlreadyExistException;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkAlreadyRegisteredInChatException;
import edu.java.exception.LinkNotFoundByUrlException;
import edu.java.repository.ScrapperRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperService {

    private final ScrapperRepository repository;

    public void saveChatById(Long tgChatId) {
        if (repository.isChatAlreadyExist(tgChatId)) {
            throw new ChatAlreadyExistException();
        }
        repository.saveByChatId(tgChatId);
    }

    public void deleteChatById(Long tgChatId) {
        if (!repository.isChatAlreadyExist(tgChatId)) {
            throw new ChatNotFoundException();
        }
        repository.deleteChatByChatId(tgChatId);
    }

    public ListLinksResponse getLinksByChatId(Long tgChatId) {
        if (!repository.isChatAlreadyExist(tgChatId)) {
            throw new ChatNotFoundException();
        }
        var result = repository.getLinksByChatId(tgChatId);
        return new ListLinksResponse(result, result.size());
    }

    public LinkResponse addLinkByChatId(Long tgChatId, AddLinkRequest addLinkRequest) {
        if (!repository.isChatAlreadyExist(tgChatId)) {
            throw new ChatNotFoundException();
        }
        String urlLinkRequest = addLinkRequest.getUrl();
        List<LinkResponse> list = repository.getLinksByChatId(tgChatId);
        if (list.stream().anyMatch(linkResponse -> linkResponse.getUrl().equals(urlLinkRequest))) {
            throw new LinkAlreadyRegisteredInChatException();
        }
        return repository.addLinkByChatId(tgChatId, addLinkRequest);
    }

    public LinkResponse deleteLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        if (!repository.isChatAlreadyExist(tgChatId)) {
            throw new ChatNotFoundException();
        }
        List<LinkResponse> list = repository.getLinksByChatId(tgChatId);
        String url = removeLinkRequest.getUrl();
        if (list.stream().noneMatch(linkResponse -> linkResponse.getUrl().equals(url))) {
            throw new LinkNotFoundByUrlException();
        }
        return repository.deleteLink(tgChatId, removeLinkRequest);
    }

}
