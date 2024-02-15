package command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.command.UntrackCommand;
import edu.java.bot.model.Link;
import edu.java.bot.model.User;
import edu.java.bot.model.UserState;
import edu.java.bot.repository.TempRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;

public class UntrackCommandTest {
    private static TempRepository repository;
    private static Update update;
    private static Message message;
    private static Chat chat;

    @BeforeAll
    public static void mockInit() {
        repository = Mockito.mock(TempRepository.class);
        update = Mockito.mock(Update.class);
        message = Mockito.mock(Message.class);
        chat = Mockito.mock(Chat.class);
    }

    @Test
    public void notRegisteredUserShouldRegisterFirst() {
        UntrackCommand untrackCommand = new UntrackCommand(repository);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/untrack");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(message.chat().id()).thenReturn(13L);
        Mockito.when(repository.findByChatId(Mockito.anyLong())).thenReturn(Optional.empty());

        String actualResponseMessage = untrackCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessage = "Please, register! Use command */start*.";

        Assertions.assertEquals(actualResponseMessage, expectedResponseMessage);
    }

    @Test
    public void neutralUserShouldAddLinkToUntrack() {
        UntrackCommand untrackCommand = new UntrackCommand(repository);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/untrack");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(13L);
        Mockito.when(repository.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(new User()));

        String actualResponseMessageToUntrack = untrackCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessageToUntrack = "Please, enter the link you want to untrack.";

        Assertions.assertEquals(actualResponseMessageToUntrack, expectedResponseMessageToUntrack);
    }

    @Test
    public void notTrackedLinkShouldNotBeUntracked() {
        UntrackCommand untrackCommand = new UntrackCommand(repository);
        Link link = new Link();
        link.setUrl("apple.com");
        User user = new User();
        user.setState(UserState.UNTRACK);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("apple.com");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(13L);
        Mockito.when(repository.wasLinkTracked(user, link)).thenReturn(false);
        Mockito.when(repository.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(user));

        String actualResponseMessageLinkWasNotTracked =
            untrackCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessageLinkWasNotTracked = "This link was not tracked.";

        Assertions.assertEquals(actualResponseMessageLinkWasNotTracked, expectedResponseMessageLinkWasNotTracked);
    }

    @Test
    public void UntrackStateShouldUntrackLink() {
        UntrackCommand untrackCommand = new UntrackCommand(repository);
        Link link = new Link();
        link.setUrl("apple.com");
        User user = new User();
        user.setState(UserState.UNTRACK);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("apple.com");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(13L);
        Mockito.when(repository.wasLinkTracked(user, link)).thenReturn(true);
        Mockito.when(repository.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(repository.findByUrl(Mockito.anyLong(), Mockito.anyString())).thenReturn(Optional.of(link));

        String actualResponseMessageUserStateUntracked =
            untrackCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessageUserStateUntracked = "The link is no longer tracked.";

        Assertions.assertEquals(actualResponseMessageUserStateUntracked, expectedResponseMessageUserStateUntracked);
    }

    @Test
    public void commandOfStartCommandShouldBeRight() {
        UntrackCommand untrackCommand = new UntrackCommand(repository);

        String actualCommand = untrackCommand.command();
        String expectedCommand = "/untrack";

        Assertions.assertEquals(actualCommand, expectedCommand);
    }

    @Test
    public void descriptionShouldBeRight() {
        UntrackCommand untrackCommand = new UntrackCommand(repository);

        String actualDescription = untrackCommand.description();
        String expectedDescription = "Stop tracking link";

        Assertions.assertEquals(actualDescription, expectedDescription);
    }
}

