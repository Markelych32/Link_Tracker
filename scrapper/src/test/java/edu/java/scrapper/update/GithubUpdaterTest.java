package edu.java.scrapper.update;

import edu.java.client.BotClient;
import edu.java.controller.dto.response.LinkUpdate;
import edu.java.domain.dto.Link;
import edu.java.github.GithubClient;
import edu.java.github.RepositoryResponse;
import edu.java.service.chat.ChatService;
import edu.java.service.link.LinkService;
import edu.java.service.update.GithubUpdater;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GithubUpdaterTest {

    @Mock
    LinkService linkService;
    @Mock
    ChatService chatService;
    @Mock
    GithubClient githubClient;
    @Mock
    BotClient botClient;
    @InjectMocks
    GithubUpdater underTest;

    @Test
    void supportOfGitLinkShouldReturnTrue() {
        final URI uri = URI.create("https://github.com/Markelych32/Git_Learning");
        assertTrue(underTest.support(uri));
    }

    @Test
    void supportOfNotGitLinkShouldReturnFalse() {
        final URI uri =
            URI.create("https://stackoverflow.com/questions/78240671/why-my-string-comparison-isnt-working-in-bash");
        assertFalse(underTest.support(uri));
    }

    @Test
    void updateNotOldLinkShouldJustUpdateLastCheck() {
        final URI uri = URI.create("https://github.com/Markelych32/Git_Learning");
        final RepositoryResponse repositoryResponse = new RepositoryResponse(
            "1", "Markelych32/Git_Learning", OffsetDateTime.parse("2024-02-01T23:07:13Z")
        );
        final Link link = new Link();
        link.setUrl("https://github.com/Markelych32/Git_Learning");
        link.setLastUpdate(OffsetDateTime.parse("2024-02-01T23:07:13Z"));
        link.setLastCheck(OffsetDateTime.parse("2024-02-01T23:07:13Z"));
        when(githubClient.fetchRepository(anyString(), anyString())).thenReturn(repositoryResponse);
        underTest.update(link);
        verify(linkService, times(1)).update(ArgumentMatchers.any(Link.class));
    }

    @Test
    void updateNewLinkShouldUpdateLastUpdateFieldAndBotUpdate() {
        final URI uri = URI.create("https://github.com/Markelych32/Git_Learning");
        final RepositoryResponse repositoryResponse = new RepositoryResponse(
            "1", "Markelych32/Git_Learning", OffsetDateTime.parse("2024-02-01T23:07:13Z")
        );
        final Link link = new Link();
        link.setUrl("https://github.com/Markelych32/Git_Learning");
        link.setLastCheck(OffsetDateTime.parse("2024-02-01T23:07:13Z"));
        when(githubClient.fetchRepository(anyString(), anyString())).thenReturn(repositoryResponse);
        underTest.update(link);
        verify(linkService, times(1)).update(ArgumentMatchers.any(Link.class));
        verify(botClient, times(1)).updateLink(ArgumentMatchers.any(LinkUpdate.class));
    }
}
