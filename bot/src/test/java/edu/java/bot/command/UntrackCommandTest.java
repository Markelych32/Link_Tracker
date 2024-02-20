package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.UntrackCommand;
import edu.java.bot.model.Link;
import edu.java.bot.model.User;
import edu.java.bot.model.UserState;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import edu.java.bot.service.LinkService;
import edu.java.bot.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UntrackCommandTest {
    private static UserService userService;
    private static LinkService linkService;
    private static Update update;
    private static Message message;
    private static Chat chat;

    @BeforeAll
    public static void mockInit() {
        userService = mock(UserService.class);
        linkService = mock(LinkService.class);
        update = mock(Update.class);
        message = mock(Message.class);
        chat = mock(Chat.class);
    }
    @Test
    public void neutralUserShouldAddLinkToUntrack() {
        UntrackCommand untrackCommand = new UntrackCommand(userService, linkService);

        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/untrack");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(13L);
        when(userService.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(new User()));

        String actualResponseMessageToUntrack = untrackCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessageToUntrack = "Please, enter the link you want to untrack.";

        Assertions.assertEquals(actualResponseMessageToUntrack, expectedResponseMessageToUntrack);
    }

    @Test
    public void notTrackedLinkShouldNotBeUntracked() {
        UntrackCommand untrackCommand = new UntrackCommand(userService, linkService);
        Link link = new Link();
        link.setUrl("apple.com");
        User user = new User();
        user.setState(UserState.UNTRACK);

        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("apple.com");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(13L);
        when(userService.wasLinkTracked(user, link)).thenReturn(false);
        when(userService.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(user));

        String actualResponseMessageLinkWasNotTracked =
            untrackCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessageLinkWasNotTracked = "This link was not tracked.";

        Assertions.assertEquals(actualResponseMessageLinkWasNotTracked, expectedResponseMessageLinkWasNotTracked);
    }

    @Test
    public void UntrackStateShouldUntrackLink() {
        UntrackCommand untrackCommand = new UntrackCommand(userService, linkService);
        Link link = new Link();
        link.setUrl("apple.com");
        User user = new User();
        user.setLinks(new ArrayList<>(List.of(link)));
        user.setState(UserState.UNTRACK);

        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("apple.com");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(13L);
        when(userService.wasLinkTracked(user, link)).thenReturn(true);
        when(userService.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(linkService.findByUrl(Mockito.anyString())).thenReturn(Optional.of(link));

        String actualResponseMessageUserStateUntracked =
            untrackCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessageUserStateUntracked = "The link is no longer tracked.";

        Assertions.assertEquals(actualResponseMessageUserStateUntracked, expectedResponseMessageUserStateUntracked);
    }

    @Test
    public void commandOfStartCommandShouldBeRight() {
        UntrackCommand untrackCommand = new UntrackCommand(userService, linkService);

        String actualCommand = untrackCommand.command();
        String expectedCommand = "/untrack";

        Assertions.assertEquals(actualCommand, expectedCommand);
    }

    @Test
    public void descriptionShouldBeRight() {
        UntrackCommand untrackCommand = new UntrackCommand(userService, linkService);

        String actualDescription = untrackCommand.description();
        String expectedDescription = "Stop tracking link";

        Assertions.assertEquals(actualDescription, expectedDescription);
    }
}

