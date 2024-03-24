package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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
    private static Update update;
    private static Message message;
    private static Chat chat;

    @Mock
    ScrapperClient scrapperClient;
    @InjectMocks
    StartCommand underTest;

    @BeforeAll
    public static void mockInit() {
        update = mock(Update.class);
        message = mock(Message.class);
        chat = mock(Chat.class);
    }

    @Test
    public void registrationNewUserShouldAskNameAndCongratulate() {

    }

    @Test
    public void commandOfStartCommandShouldBeRight() {
        String actualCommand = underTest.command();
        String expectedCommand = "/start";

        Assertions.assertEquals(actualCommand, expectedCommand);
    }

    @Test
    public void descriptionShouldBeRight() {
        String actualDescription = underTest.description();
        String expectedDescription = "Register new User";

        Assertions.assertEquals(actualDescription, expectedDescription);
    }
}
