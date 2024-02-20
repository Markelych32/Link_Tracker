package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import edu.java.bot.HelpCommand;
import edu.java.bot.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class HelpCommandTest {

    private static UserService userService;

    @BeforeAll
    public static void init() {
        userService = mock(UserService.class);
    }
    @Test
    public void commandMessageShouldBeRight() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        HelpCommand helpCommand = new HelpCommand(userService);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(13L);

        String actualResponseMessage = helpCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessage = """
            * Доступные команды*:

            */help* - Вывести список доступных команд
            */track* - Начать отслеживание обновлений по ссылке
            */untrack* - Прекратить отслеживание по ссылке
            */list* - Показать список отслеживаемых ссылок
            """;

        Assertions.assertEquals(actualResponseMessage, expectedResponseMessage);
    }

    @Test
    public void commandOfHelpCommandShouldBeRight() {
        HelpCommand helpCommand = new HelpCommand(userService);

        String actualCommand = helpCommand.command();
        String expectedCommand = "/help";

        Assertions.assertEquals(expectedCommand, actualCommand);
    }

    @Test
    public void descriptionShouldBeRight() {
        HelpCommand helpCommand = new HelpCommand(userService);

        String actualDescription = helpCommand.description();
        String expectedDescription = "Show all available commands";

        Assertions.assertEquals(expectedDescription, actualDescription);
    }
}
