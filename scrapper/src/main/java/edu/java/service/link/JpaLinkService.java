package edu.java.service.link;

import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.controller.dto.response.ListLinksResponse;
import edu.java.domain.chat.ChatEntity;
import edu.java.domain.dto.jdbc.Link;
import edu.java.domain.link.LinkEntity;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkAlreadyRegisteredInChatException;
import edu.java.exception.LinkNotFoundByUrlException;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import edu.java.service.chat.JpaChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;

    private LinkEntity fromAddLinkRequestToLinkEntity(AddLinkRequest addLinkRequest) {
        LinkEntity linkEntity = new LinkEntity();
        linkEntity.setUrl(addLinkRequest.getUrl());
        linkEntity.setLastCheck(OffsetDateTime.now());
        return linkEntity;
    }

    private Link fromLinkEntityToLink(LinkEntity linkEntity) {
        return new Link(
            linkEntity.getId(),
            linkEntity.getUrl(),
            linkEntity.getLastUpdate(),
            linkEntity.getLastCheck()
        );
    }

    @Override
    public Link addLink(Long tgChatId, AddLinkRequest addLinkRequest) {
        Optional<ChatEntity> chatEntity = chatRepository.findById(tgChatId);
        final String url = addLinkRequest.getUrl();
        if (chatEntity.isEmpty()) {
            throw new ChatNotFoundException();
        }
        if (chatEntity.get().getLinks().stream().anyMatch(l -> l.getUrl().equals(url))) {
            throw new LinkAlreadyRegisteredInChatException();
        }
        Optional<LinkEntity> linkEntity = linkRepository.findByUrl(url);
        if (linkEntity.isEmpty()) {
            LinkEntity link = new LinkEntity();
            link.setUrl(url);
            link.setLastCheck(OffsetDateTime.now());
            link.addChat(chatEntity.get());
            return fromLinkEntityToLink(linkRepository.save(link));
        } else {
            linkEntity.get().addChat(chatEntity.get());
            return fromLinkEntityToLink(linkRepository.save(linkEntity.get()));
        }
    }

    @Override
    public Link removeLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        Optional<ChatEntity> chatEntity = chatRepository.findById(tgChatId);
        final String url = removeLinkRequest.getUrl();
        if (chatEntity.isEmpty()) {
            throw new ChatNotFoundException();
        }
        Optional<LinkEntity> linkEntity = linkRepository.findByUrl(url);
        if (chatEntity.get().getLinks().stream().noneMatch(l -> l.getUrl().equals(url)) || linkEntity.isEmpty()) {
            throw new LinkNotFoundByUrlException();
        }
        linkEntity.get().deleteChat(chatEntity.get());
        //linkRepository.delete(linkEntity.get());
        return fromLinkEntityToLink(linkEntity.get());
    }

    @Override
    public void update(Link link) {
    }

    @Override
    public List<Link> findOldLinks(int seconds) {
        return null;
    }

    @Override
    public ListLinksResponse listAll(long tgChatId) {
        return null;
    }
}
