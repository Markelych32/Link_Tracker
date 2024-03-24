package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.ScrapperClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ListCommandTest {
    private static Update update;
    private static Message message;
    private static Chat chat;

    @Mock
    ScrapperClient scrapperClient;
    @InjectMocks
    ListCommand underTest;

    @BeforeAll
    public static void mockInit() {
        update = Mockito.mock(Update.class);
        message = Mockito.mock(Message.class);
        chat = Mockito.mock(Chat.class);
    }

    @Test
    public void emptyListOfTrackedLinksShouldShowCorrespondingMessage() {

    }

    @Test
    public void commandShouldShowAllTrackedLinksByUser() {

    }

    @Test
    public void commandOfListCommandShouldBeRight() {
        String actualCommand = underTest.command();
        String expectedCommand = "/list";
        Assertions.assertEquals(actualCommand, expectedCommand);
    }

    @Test
    public void descriptionShouldBeRight() {
        String actualDescription = underTest.description();
        String expectedDescription = "Show all tracked links";
        Assertions.assertEquals(actualDescription, expectedDescription);
    }
}
