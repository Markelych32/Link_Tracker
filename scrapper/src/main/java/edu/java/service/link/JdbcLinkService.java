package edu.java.service.link;

import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.controller.dto.response.LinkResponse;
import edu.java.controller.dto.response.ListLinksResponse;
import edu.java.domain.chat.JdbcChatDao;
import edu.java.domain.chat_link.JdbcChatLinkDao;
import edu.java.domain.dto.Link;
import edu.java.domain.link.JdbcLinkDao;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkAlreadyRegisteredInChatException;
import edu.java.exception.LinkNotFoundByUrlException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JdbcLinkService implements LinkService {

    private final JdbcLinkDao linkDao;
    private final JdbcChatDao chatDao;
    private final JdbcChatLinkDao chatLinkDao;

    @Override
    @Transactional
    public Link addLink(Long tgChatId, AddLinkRequest addLinkRequest) {
        if (chatDao.find(tgChatId).isEmpty()) {
            throw new ChatNotFoundException();
        }
        final String linkUrl = addLinkRequest.getUrl();
        final Optional<Link> findLink = linkDao.find(linkUrl);
        if (findLink.isPresent()) {
            if (chatLinkDao.find(tgChatId, findLink.get().getId()).isPresent()) {
                throw new LinkAlreadyRegisteredInChatException();
            }
            chatLinkDao.add(tgChatId, findLink.get().getId());
            return findLink.get();
        }
        Link link = new Link();
        link.setLastCheck(OffsetDateTime.now());
        link.setUrl(linkUrl);
        Long linkId = linkDao.add(link);
        chatLinkDao.add(tgChatId, linkId);
        link.setId(linkId);
        return link;
    }

    @Override
    @Transactional
    public Link removeLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        if (chatDao.find(tgChatId).isEmpty()) {
            throw new ChatNotFoundException();
        }
        final String linkUrl = removeLinkRequest.getUrl();
        final Optional<Link> findLink = linkDao.find(linkUrl);
        if (findLink.isEmpty()) {
            throw new LinkNotFoundByUrlException();
        }
        chatLinkDao.remove(tgChatId, findLink.get());
        if (!chatLinkDao.isLinkPresent(findLink.get())) {
            linkDao.remove(linkUrl);
        }
        return findLink.get();
    }

    @Override
    public void update(Link link) {
        linkDao.update(link);
    }

    @Override
    public List<Link> findOldLinks(int seconds) {
        return linkDao.findAll()
            .stream()
            .filter(
                link -> ChronoUnit.SECONDS.between(
                    link.getLastCheck(), OffsetDateTime.now()
                ) >= seconds
            ).toList();
    }

    @Override
    public ListLinksResponse listAll(long tgChatId) {
        if (chatDao.find(tgChatId).isEmpty()) {
            throw new ChatNotFoundException();
        }
        List<LinkResponse> links = linkDao.findAllByChat(tgChatId).stream()
            .map(link -> new LinkResponse(link.getId(), link.getUrl())).toList();
        return new ListLinksResponse(links, links.size());
    }
}
