package edu.java.service.link;

import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.controller.dto.response.LinkResponse;
import edu.java.controller.dto.response.ListLinksResponse;
import edu.java.domain.chat.ChatEntity;
import edu.java.domain.dto.jdbc.Link;
import edu.java.domain.link.LinkEntity;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkAlreadyRegisteredInChatException;
import edu.java.exception.LinkNotFoundByUrlException;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
public class JpaLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;

    private Link fromLinkEntityToLink(LinkEntity linkEntity) {
        return new Link(
            linkEntity.getId(),
            linkEntity.getUrl(),
            linkEntity.getLastUpdate(),
            linkEntity.getLastCheck()
        );
    }

    @Override
    @Transactional
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
    @Transactional
    public Link removeLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        Optional<ChatEntity> chatEntity = chatRepository.findById(tgChatId);
        if (chatEntity.isEmpty()) {
            throw new ChatNotFoundException();
        }
        final String url = removeLinkRequest.getUrl();
        Optional<LinkEntity> linkEntity = linkRepository.findByUrl(url);
        if (linkEntity.isEmpty() || !chatEntity.get().getLinks().contains(linkEntity.get())) {
            throw new LinkNotFoundByUrlException();
        }
        linkEntity.get().deleteChat(chatEntity.get());
        if (linkEntity.get().getChats().isEmpty()) {
            linkRepository.delete(linkEntity.get());
            return fromLinkEntityToLink(linkEntity.get());
        } else {
            return fromLinkEntityToLink(linkRepository.save(linkEntity.get()));
        }
    }

    @Override
    @Transactional
    public void update(Link link) {
        final String url = link.getUrl();
        Optional<LinkEntity> optionalLinkEntity = linkRepository.findByUrl(url);
        if (optionalLinkEntity.isEmpty()) {
            throw new LinkNotFoundByUrlException();
        }
        LinkEntity linkEntity = optionalLinkEntity.get();
        linkEntity.setLastUpdate(link.getLastUpdate());
        linkEntity.setLastCheck(link.getLastCheck());
        linkRepository.save(linkEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Link> findOldLinks(int seconds) {
        return linkRepository.findAll()
            .stream()
            .filter(
                l -> ChronoUnit.SECONDS.between(
                    l.getLastCheck(), OffsetDateTime.now()
                ) >= seconds
            ).map(l -> new Link(
                l.getId(),
                l.getUrl(),
                l.getLastUpdate(),
                l.getLastCheck()
            ))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ListLinksResponse listAll(long tgChatId) {
        Optional<ChatEntity> optionalChatEntity = chatRepository.findById(tgChatId);
        if (optionalChatEntity.isEmpty()) {
            throw new ChatNotFoundException();
        }
        ListLinksResponse listLinksResponse = new ListLinksResponse();
        listLinksResponse.setSize(optionalChatEntity.get().getLinks().size());
        listLinksResponse.setLinks(new ArrayList<>());
        for (LinkEntity linkEntity : optionalChatEntity.get().getLinks()) {
            listLinksResponse.getLinks().add(new LinkResponse(
                linkEntity.getId(),
                linkEntity.getUrl()
            ));
            log.info(listLinksResponse.getLinks().toString());
        }
        return listLinksResponse;
    }
}
