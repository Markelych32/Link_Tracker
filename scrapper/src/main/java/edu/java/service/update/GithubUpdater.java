package edu.java.service.update;

import edu.java.client.BotClient;
import edu.java.controller.dto.response.LinkUpdate;
import edu.java.domain.dto.Link;
import edu.java.github.GithubClient;
import edu.java.github.RepositoryResponse;
import edu.java.service.chat.ChatService;
import edu.java.service.link.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GithubUpdater implements LinkUpdater {

    private static final int NUM_OF_AUTHOR = 1;
    private static final int NUM_OF_REPO = 2;

    private final LinkService linkService;
    private final ChatService chatService;
    private final GithubClient githubClient;
    private final BotClient botClient;

    @Override
    public void update(Link link) {
        URI uri = URI.create(link.getUrl());
        String[] path = uri.getPath().split("/");
        RepositoryResponse response = githubClient.fetchRepository(path[NUM_OF_AUTHOR], path[NUM_OF_REPO]);
        if (link.getLastUpdate() == null || response.updatedAt().isAfter(link.getLastCheck())) {
            link.setLastUpdate(response.updatedAt());
            link.setLastCheck(OffsetDateTime.now());
            linkService.update(link);
            log.info("Bot Client updating...");
            botClient.updateLink(
                new LinkUpdate(
                    link.getUrl(),
                    String.format("link: %s is updated", link.getUrl()),
                    chatService.findChatsByLink(link)
                )
            );
        } else {
            link.setLastCheck(OffsetDateTime.now());
            linkService.update(link);
        }
    }

    @Override
    public boolean support(URI link) {
        try {
            return link.getHost().equals("github.com");
        } catch (Exception e) {
            return false;
        }
    }
}
