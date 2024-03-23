package edu.java.service.link;

import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.controller.dto.response.ListLinksResponse;
import edu.java.domain.dto.jdbc.Link;
import java.util.List;

public interface LinkService {
    Link addLink(Long tgChatId, AddLinkRequest addLinkRequest);

    Link removeLink(Long tgChatId, RemoveLinkRequest removeLinkRequest);

    void update(Link link);

    List<Link> findOldLinks(int seconds);

    ListLinksResponse listAll(long tgChatId);
}
