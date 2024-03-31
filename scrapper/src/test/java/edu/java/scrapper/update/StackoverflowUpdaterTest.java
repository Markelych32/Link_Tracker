package edu.java.scrapper.update;

import edu.java.client.BotClient;
import edu.java.controller.dto.response.LinkUpdate;
import edu.java.domain.dto.jdbc.Link;
import edu.java.service.chat.ChatService;
import edu.java.service.link.LinkService;
import edu.java.service.update.StackoverflowUpdater;
import edu.java.stackOverflow.ItemResponse;
import edu.java.stackOverflow.StackOverflowClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.net.URI;
import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StackoverflowUpdaterTest {
    @Mock
    LinkService linkService;
    @Mock
    ChatService chatService;
    @Mock
    StackOverflowClient stackOverflowClient;
    @Mock
    BotClient botClient;
    @InjectMocks
    StackoverflowUpdater underTest;

    @Test
    void supportIncorrectLinkShouldReturnFalse() {
        final URI uri = URI.create("https://github.com/Markelych32/Git_Learning");
        assertFalse(underTest.support(uri));
    }

    @Test
    void supportCorrectLinkShouldReturnTrue() {
        final URI uri =
            URI.create("https://stackoverflow.com/questions/78240671/why-my-string-comparison-isnt-working-in-bash");
        assertTrue(underTest.support(uri));
    }

    @Test
    void updateFreshLinkShouldJustUpdateLastCheck() {
        final URI uri =
            URI.create("https://stackoverflow.com/questions/78240671/why-my-string-comparison-isnt-working-in-bash");
        ItemResponse itemResponse = new ItemResponse("Test title", OffsetDateTime.now());
        final Link link = new Link();
        link.setUrl("https://stackoverflow.com/questions/78240671/why-my-string-comparison-isnt-working-in-bash");
        link.setLastUpdate(OffsetDateTime.now());
        link.setLastCheck(OffsetDateTime.now());
        when(stackOverflowClient.fetchQuestion(ArgumentMatchers.anyString())).thenReturn(itemResponse);
        underTest.update(link);
        verify(linkService, times(1)).update(ArgumentMatchers.any(Link.class));
    }

    @Test
    void updateOldLinkShouldUpdateAndBotUpdateLink() {
        final URI uri =
            URI.create("https://stackoverflow.com/questions/78240671/why-my-string-comparison-isnt-working-in-bash");
        ItemResponse itemResponse = new ItemResponse("Test title", OffsetDateTime.now());
        final Link link = new Link();
        link.setUrl("https://stackoverflow.com/questions/78240671/why-my-string-comparison-isnt-working-in-bash");
        link.setLastUpdate(OffsetDateTime.parse("2024-02-01T23:07:13Z"));
        link.setLastCheck(OffsetDateTime.now());
        when(stackOverflowClient.fetchQuestion(ArgumentMatchers.anyString())).thenReturn(itemResponse);
        underTest.update(link);
        verify(linkService, times(1)).update(ArgumentMatchers.any(Link.class));
        verify(botClient, times(1)).updateLink(ArgumentMatchers.any(LinkUpdate.class));
    }
}
