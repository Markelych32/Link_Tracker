package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import edu.java.bot.TestData;
import edu.java.bot.client.ScrapperClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class StartCommandTest {

    @Mock
    ScrapperClient scrapperClient;
    @Mock
    Update update;
    @Mock
    Message message;
    @Mock
    Chat chat;
    @InjectMocks
    StartCommand underTest;

    @Test
    void afterRegistrationShouldWriteCongratulationMessage() {
        final Long chatId = TestData.TEST_ID;
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        doNothing().when(scrapperClient).registerChat(anyLong());
        final String expectedResult = """
            Our Congratulations!
            You have been registered.
            Use command */help* to know what Bot can do.
            """;
        final String actualResult = underTest.handle(update).getParameters().get("text").toString();
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void commandOfStartCommandShouldBeRight() {
        String actualCommand = underTest.command();
        String expectedCommand = "/start";

        Assertions.assertEquals(actualCommand, expectedCommand);
    }

    @Test
    void descriptionShouldBeRight() {
        String actualDescription = underTest.description();
        String expectedDescription = "Register new User";

        Assertions.assertEquals(actualDescription, expectedDescription);
    }
}
