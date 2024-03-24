package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import edu.java.bot.TestData;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controller.dto.request.AddLinkRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrackCommandTest {

    @Mock
    ScrapperClient scrapperClient;
    @Mock
    private static Update update;
    @Mock
    private static Message message;
    @Mock
    private static Chat chat;
    @InjectMocks
    TrackCommand underTest;

    @Test
    public void afterTrackingLinkShouldWriteMessage() {
        final Long chatId = TestData.TEST_ID;
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/track test");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        doNothing().when(scrapperClient).addLink(anyLong(), ArgumentMatchers.any(AddLinkRequest.class));
        final String expectedResult = "The Link is now being tracked.";
        final String actualResult = underTest.handle(update).getParameters().get("text").toString();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void commandOfTrackCommandShouldBeRight() {
        String actualCommand = underTest.command();
        String expectedCommand = "/track";

        assertEquals(actualCommand, expectedCommand);
    }

    @Test
    public void descriptionShouldBeRight() {
        String actualDescription = underTest.description();
        String expectedDescription = "Start track the link";

        assertEquals(actualDescription, expectedDescription);
    }
}
