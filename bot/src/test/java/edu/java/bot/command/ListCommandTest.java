package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.TestData;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controller.dto.response.LinkResponse;
import edu.java.bot.controller.dto.response.ListLinksResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListCommandTest {
    @Mock
    private static Update update;
    @Mock
    private static Message message;
    @Mock
    private static Chat chat;

    @Mock
    ScrapperClient scrapperClient;
    @InjectMocks
    ListCommand underTest;

    @Test
    public void emptyListOfTrackedLinksShouldShowCorrespondingMessage() {
        final Long chatId = TestData.TEST_ID;
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(scrapperClient.getLinks(anyLong())).thenReturn(new ListLinksResponse());
        ListLinksResponse listLinksResponse = new ListLinksResponse(
            List.of(new LinkResponse()), 0);
        final String expectedResult = """
            Ops... There are no tracked links.
            You can add them using */track* command.
            """;
        final String actualResult = underTest.handle(update).getParameters().get("text").toString();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void commandShouldShowAllTrackedLinksByUser() {
        final Long chatId = TestData.TEST_ID;
        ListLinksResponse listLinksResponse = new ListLinksResponse(
            List.of(new LinkResponse(1L, "apple.com"), new LinkResponse(2L, "google.com")), 2);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(scrapperClient.getLinks(anyLong())).thenReturn(listLinksResponse);
        final String expectedResult = """
            *Tracked Links*:

            apple.com
            google.com""";
        final String actualResult = underTest.handle(update).getParameters().get("text").toString();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void commandOfListCommandShouldBeRight() {
        String actualCommand = underTest.command();
        String expectedCommand = "/list";
        assertEquals(actualCommand, expectedCommand);
    }

    @Test
    public void descriptionShouldBeRight() {
        String actualDescription = underTest.description();
        String expectedDescription = "Show all tracked links";
        assertEquals(actualDescription, expectedDescription);
    }
}
