package edu.java.service.link;

import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.controller.dto.response.ListLinksResponse;
import edu.java.domain.dto.Link;
import java.util.List;

public interface LinkService {
    Link addLink(Long tgChatId, AddLinkRequest addLinkRequest);

    Link removeLink(Long tgChatId, RemoveLinkRequest removeLinkRequest);

    boolean update(Link link);

    List<Link> findOldLinks(long seconds);

    ListLinksResponse listAll(long tgChatId);
}