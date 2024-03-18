package edu.java.service.update;

import edu.java.domain.dto.Link;
import edu.java.github.GithubClient;
import edu.java.github.RepositoryResponse;
import edu.java.service.chat.ChatService;
import edu.java.service.link.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GithubUpdater implements LinkUpdater {

    private final LinkService linkService;
    private final ChatService chatService;
    private final GithubClient githubClient;
    //private final BotClient botClient;

    @Override
    public void update(Link link) {
        URI uri = URI.create(link.getUrl());
        String[] path = uri.getPath().split("/");
        RepositoryResponse response = githubClient.fetchRepository(path[1], path[2]);
        if (link.getLastUpdate() == null || response.updatedAt().isAfter(link.getLastUpdate())) {
            link.setLastUpdate(response.updatedAt());
            link.setLastCheck(OffsetDateTime.now());
            linkService.update(link);
//            botClient.updateLink(
//                new LinkUpdate(
//                    link.getUrl(),
//                    String.format("link: %s is updated", link.getUrl()),
//                    chatService.findChatsByLink(link)
//                )
//            );
        } else {
            link.setLastCheck(OffsetDateTime.now());
            linkService.update(link);
        }
    }

    @Override
    public boolean support(URI link) {
        return link.getHost().equals("github.com");
    }
}