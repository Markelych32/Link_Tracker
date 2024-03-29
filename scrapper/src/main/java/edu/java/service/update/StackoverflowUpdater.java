package edu.java.service.update;

import edu.java.client.BotClient;
import edu.java.controller.dto.response.LinkUpdate;
import edu.java.domain.dto.Link;
import edu.java.service.chat.ChatService;
import edu.java.service.link.LinkService;
import edu.java.stackOverflow.ItemResponse;
import edu.java.stackOverflow.StackOverflowClient;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StackoverflowUpdater implements LinkUpdater {

    private final LinkService linkService;
    private final ChatService chatService;
    private final StackOverflowClient stackOverflowClient;
    private final BotClient botClient;

    @Override
    public void update(Link link) {
        URI uri = URI.create(link.getUrl());
        String[] path = uri.getPath().split("/");
        ItemResponse itemResponse = stackOverflowClient.fetchQuestion(path[2]);
        if (link.getLastUpdate() == null || itemResponse.lastActivityDate().isAfter(link.getLastUpdate())) {
            link.setLastUpdate(itemResponse.lastActivityDate());
            link.setLastCheck(OffsetDateTime.now());
            linkService.update(link);
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
            return link.getHost().equals("stackoverflow.com");
        } catch (Exception e) {
            return false;
        }
    }
}
