package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.Link;
import edu.java.bot.model.User;
import edu.java.bot.service.LinkService;
import edu.java.bot.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;

public class ListCommandTest {
    private static UserService userService;
    private static Update update;
    private static Message message;
    private static Chat chat;
    private static LinkService linkService;

    @BeforeAll
    public static void mockInit() {
        userService = Mockito.mock(UserService.class);
        update = Mockito.mock(Update.class);
        message = Mockito.mock(Message.class);
        chat = Mockito.mock(Chat.class);
        linkService = mock(LinkService.class);
    }

    @Test
    public void emptyListOfTrackedLinksShouldShowCorrespondingMessage() {
        User user = new User();
        user.setLinks(new ArrayList<>());
        ListCommand listCommand = new ListCommand(userService);

        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/list");
        when(message.chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(1L);
        when(userService.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(user));

        final String actualResponseMessage = listCommand.handle(update).getParameters().get("text").toString();
        final String expectedResponseMessage =
            """
                Ops... There are no tracked links.
                You can add them using */track* command.
                """;

        Assertions.assertEquals(actualResponseMessage, expectedResponseMessage);
    }

    @Test
    public void commandShouldShowAllTrackedLinksByUser() {

        ListCommand listCommand = new ListCommand(userService);
        Link apple_link = new Link();
        Link yandex_link = new Link();
        User user = new User();
        apple_link.setUrl("apple.com");
        yandex_link.setUrl("yandex.ru");
        user.setLinks(new ArrayList<>(List.of(apple_link, yandex_link)));

        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/list");
        when(message.chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(13L);
        when(userService.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(user));

        String actualResponseMessage = listCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessage = """
            *Tracked Links*:

            apple.com
            yandex.ru""";

        Assertions.assertEquals(actualResponseMessage, expectedResponseMessage);
    }

    @Test
    public void commandOfListCommandShouldBeRight() {
        ListCommand listCommand = new ListCommand(userService);

        String actualCommand = listCommand.command();
        String expectedCommand = "/list";

        Assertions.assertEquals(actualCommand, expectedCommand);
    }

    @Test
    public void descriptionShouldBeRight() {
        ListCommand listCommand = new ListCommand(userService);

        String actualDescription = listCommand.description();
        String expectedDescription = "Show all tracked links";

        Assertions.assertEquals(actualDescription, expectedDescription);
    }
}
