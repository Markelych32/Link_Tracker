package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import static org.mockito.Mockito.mock;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrackCommandTest {
    private static Update update;
    private static Message message;
    private static Chat chat;

    @Mock
    ScrapperClient scrapperClient;
    @InjectMocks
    TrackCommand underTest;

    @BeforeAll
    public static void mockInit() {
        update = mock(Update.class);
        message = mock(Message.class);
        chat = mock(Chat.class);
    }

    @Test
    public void notTrackedLinkShouldBeTracked() {
    }

    @Test
    public void userWithTrackStateShouldNotify() {
    }

    @Test
    public void commandOfTrackCommandShouldBeRight() {
        String actualCommand = underTest.command();
        String expectedCommand = "/track";

        Assertions.assertEquals(actualCommand, expectedCommand);
    }

    @Test
    public void descriptionShouldBeRight() {
        String actualDescription = underTest.description();
        String expectedDescription = "Start track the link";

        Assertions.assertEquals(actualDescription, expectedDescription);
    }
}
