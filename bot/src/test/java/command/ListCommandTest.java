package command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.command.ListCommand;
import edu.java.bot.model.Link;
import edu.java.bot.model.User;
import edu.java.bot.repository.TempRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListCommandTest {
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
        ListCommand listCommand = new ListCommand(repository);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/list");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(message.chat().id()).thenReturn(13L);
        Mockito.when(repository.findByChatId(Mockito.anyLong())).thenReturn(Optional.empty());
        String actualResponseMessage = listCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessage = "Please, register! Use command */start*.";

        Assertions.assertEquals(actualResponseMessage, expectedResponseMessage);
    }

    @Test
    public void emptyListOfTrackedLinksShouldShowCorrespondingMessage() {
        User user = new User();
        ListCommand listCommand = new ListCommand(repository);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/list");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(message.chat().id()).thenReturn(13L);
        Mockito.when(repository.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(user));
        String actualResponseMessage = listCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessage =
            """
                Ops... There are no tracked links.
                You can add them using */track* command.
                """;

        Assertions.assertEquals(actualResponseMessage, expectedResponseMessage);
    }

    @Test
    public void commandShouldShowAllTrackedLinksByUser() {
        ListCommand listCommand = new ListCommand(repository);
        Link apple_link = new Link();
        Link yandex_link = new Link();
        User user = new User();
        apple_link.setUrl("apple.com");
        yandex_link.setUrl("yandex.ru");

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/list");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(message.chat().id()).thenReturn(13L);
        Mockito.when(repository.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(repository.getLinks(Mockito.anyLong())).thenReturn(List.of(apple_link, yandex_link));
        user.setLinks(new ArrayList<>(List.of(apple_link, yandex_link)));

        String actualResponseMessage = listCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessage = """
            *Tracked Links*:

            apple.com
            yandex.ru""";

        Assertions.assertEquals(actualResponseMessage, expectedResponseMessage);
    }

    @Test
    public void commandOfListCommandShouldBeRight() {
        ListCommand listCommand = new ListCommand(repository);

        String actualCommand = listCommand.command();
        String expectedCommand = "/list";

        Assertions.assertEquals(actualCommand, expectedCommand);
    }

    @Test
    public void descriptionShouldBeRight() {
        ListCommand listCommand = new ListCommand(repository);

        String actualDescription = listCommand.description();
        String expectedDescription = "Show all tracked links";

        Assertions.assertEquals(actualDescription, expectedDescription);
    }
}
