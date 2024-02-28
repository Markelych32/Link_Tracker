package edu.java.service;

import edu.java.controller.dto.AddLinkRequest;
import edu.java.controller.dto.LinkResponse;
import edu.java.controller.dto.ListLinksResponse;
import edu.java.controller.dto.RemoveLinkRequest;
import edu.java.exception.ChatAlreadyExistException;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkAlreadyRegisteredInChatException;
import edu.java.exception.LinkNotFoundByUrlException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.java.repository.ScrapperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperService {

    private final ScrapperRepository repository;

    public void saveChatById(Long tgChatId) {
        repository.saveByChatId(tgChatId);
    }

    public void deleteTgChatById(Long tgChatId) {
        repository.deleteChatByChatId(tgChatId);
    }

    public ListLinksResponse getLinksByChatId(Long tgChatId) {
        return repository.getLinksByChatId(tgChatId);
    }

    public LinkResponse addLinkByChatId(Long tgChatId, AddLinkRequest addLinkRequest) {
        return repository.addLinkByChatId(tgChatId, addLinkRequest);
    }

    public LinkResponse deleteLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        return repository.deleteLink(tgChatId, removeLinkRequest);
    }

}
