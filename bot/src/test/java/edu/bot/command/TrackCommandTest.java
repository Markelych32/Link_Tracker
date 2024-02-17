package edu.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.model.Link;
import edu.java.bot.model.User;
import edu.java.bot.model.UserState;
import edu.java.bot.repository.TempRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;

public class TrackCommandTest {
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
        TrackCommand trackCommand = new TrackCommand(repository);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/track");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(message.chat().id()).thenReturn(13L);
        Mockito.when(repository.findByChatId(Mockito.anyLong())).thenReturn(Optional.empty());

        String actualResponseMessage = trackCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessage = "Please, register! Use command */start*.";

        Assertions.assertEquals(actualResponseMessage, expectedResponseMessage);
    }

    @Test
    public void notTrackedLinkShouldBeTracked() {
        TrackCommand trackCommand = new TrackCommand(repository);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(message.text()).thenReturn("/track");
        Mockito.when(chat.id()).thenReturn(13L);
        Mockito.when(repository.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(new User()));

        String actualResponseMessageAskTrackLink = trackCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessageAskTrackLink = "Please, enter the* link *you want to track.";

        Assertions.assertEquals(actualResponseMessageAskTrackLink, expectedResponseMessageAskTrackLink);
    }

    @Test
    public void userWithTrackStateShouldNotify() {
        TrackCommand trackCommand = new TrackCommand(repository);
        Link link = new Link();
        link.setUrl("apple.com");
        User user = new User();
        user.setState(UserState.TRACK);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("apple.com");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(13L);
        Mockito.when(repository.findByUrl(13L, "apple.com")).thenReturn(Optional.of(link));
        Mockito.when(repository.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(user));

        String actualResponseMessageWhenUserStateTrack =
            trackCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessageWhenUserStateTrack = "The Link is now being tracked.";

        Assertions.assertEquals(actualResponseMessageWhenUserStateTrack, expectedResponseMessageWhenUserStateTrack);
    }

    @Test
    public void commandOfTrackCommandShouldBeRight() {
        TrackCommand trackCommand = new TrackCommand(repository);

        String actualCommand = trackCommand.command();
        String expectedCommand = "/track";

        Assertions.assertEquals(actualCommand, expectedCommand);
    }

    @Test
    public void descriptionShouldBeRight() {
        TrackCommand trackCommand = new TrackCommand(repository);

        String actualDescription = trackCommand.description();
        String expectedDescription = "Start track the link";

        Assertions.assertEquals(actualDescription, expectedDescription);
    }
}
