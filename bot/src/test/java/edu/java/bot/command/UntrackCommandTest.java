package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import edu.java.bot.TestData;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controller.dto.request.AddLinkRequest;
import edu.java.bot.controller.dto.request.RemoveLinkRequest;
import edu.java.bot.controller.dto.response.LinkResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UntrackCommandTest {

    @Mock
    ScrapperClient scrapperClient;
    @Mock
    Chat chat;
    @Mock
    Update update;
    @Mock
    Message message;
    @InjectMocks
    UntrackCommand underTest;

    @Test
    public void afterUntrackLinkShouldWriteMessage() {
        final Long chatId = TestData.TEST_ID;
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(message.text()).thenReturn("/untrack test");
        when(chat.id()).thenReturn(chatId);
        when(scrapperClient.deleteLink(anyLong(), ArgumentMatchers.any(RemoveLinkRequest.class))).thenReturn(new LinkResponse());
        final String expectedResult = "The link is no longer tracked.";
        final String actualResult = underTest.handle(update).getParameters().get("text").toString();
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void commandOfStartCommandShouldBeRight() {
        String actualCommand = underTest.command();
        String expectedCommand = "/untrack";

        Assertions.assertEquals(actualCommand, expectedCommand);
    }

    @Test
    public void descriptionShouldBeRight() {
        String actualDescription = underTest.description();
        String expectedDescription = "Stop tracking link";

        Assertions.assertEquals(actualDescription, expectedDescription);
    }
}

