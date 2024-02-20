package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.model.Link;
import edu.java.bot.model.User;
import edu.java.bot.model.UserState;
import edu.java.bot.service.LinkService;
import edu.java.bot.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrackCommandTest {
    private static UserService userService;
    private static LinkService linkService;
    private static Update update;
    private static Message message;
    private static Chat chat;

    @BeforeAll
    public static void mockInit() {
        userService = Mockito.mock(UserService.class);
        linkService = mock(LinkService.class);
        update = Mockito.mock(Update.class);
        message = Mockito.mock(Message.class);
        chat = Mockito.mock(Chat.class);
    }
    @Test
    public void notTrackedLinkShouldBeTracked() {
        TrackCommand trackCommand = new TrackCommand(userService, linkService);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(message.text()).thenReturn("/track");
        when(chat.id()).thenReturn(13L);
        when(userService.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(new User()));

        String actualResponseMessageAskTrackLink = trackCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessageAskTrackLink = "Please, enter the* link *you want to track.";

        Assertions.assertEquals(actualResponseMessageAskTrackLink, expectedResponseMessageAskTrackLink);
    }

    @Test
    public void userWithTrackStateShouldNotify() {
        TrackCommand trackCommand = new TrackCommand(userService, linkService);
        Link link = new Link();
        link.setUrl("apple.com");
        User user = new User();
        user.setState(UserState.TRACK);

        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("apple.com");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(13L);
        when(linkService.findByUrl("apple.com")).thenReturn(Optional.of(link));
        when(userService.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(user));

        String actualResponseMessageWhenUserStateTrack =
            trackCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessageWhenUserStateTrack = "The Link is now being tracked.";

        Assertions.assertEquals(actualResponseMessageWhenUserStateTrack, expectedResponseMessageWhenUserStateTrack);
    }

    @Test
    public void commandOfTrackCommandShouldBeRight() {
        TrackCommand trackCommand = new TrackCommand(userService, linkService);

        String actualCommand = trackCommand.command();
        String expectedCommand = "/track";

        Assertions.assertEquals(actualCommand, expectedCommand);
    }

    @Test
    public void descriptionShouldBeRight() {
        TrackCommand trackCommand = new TrackCommand(userService, linkService);

        String actualDescription = trackCommand.description();
        String expectedDescription = "Start track the link";

        Assertions.assertEquals(actualDescription, expectedDescription);
    }
}
