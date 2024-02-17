package edu.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.command.HelpCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class HelpCommandTest {
    @Test
    public void commandMessageShouldBeRight() {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        HelpCommand helpCommand = new HelpCommand();

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(13L);

        String actualResponseMessage = helpCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessage = """
            * Доступные команды*:

            */start* - Зарегистрировать нового пользователя
            */help* - Вывести список доступных команд
            */track* - Начать отслеживание обновлений по ссылке
            */untrack* - Прекратить отслеживание по ссылке
            */list* - Показать список отслеживаемых ссылок
            """;

        Assertions.assertEquals(actualResponseMessage, expectedResponseMessage);

    }

    @Test
    public void commandOfHelpCommandShouldBeRight() {
        HelpCommand helpCommand = new HelpCommand();

        String actualCommand = helpCommand.command();
        String expectedCommand = "/help";

        Assertions.assertEquals(expectedCommand, actualCommand);
    }

    @Test
    public void descriptionShouldBeRight() {
        HelpCommand helpCommand = new HelpCommand();

        String actualDescription = helpCommand.description();
        String expectedDescription = "Show all available commands";

        Assertions.assertEquals(expectedDescription, actualDescription);
    }
}
