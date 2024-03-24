package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelpCommandTest {
    @Test
    void commandMessageShouldBeRight() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        HelpCommand underTest = new HelpCommand();

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(13L);

        String actualResponseMessage = underTest.handle(update).getParameters().get("text").toString();
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
    void commandOfHelpCommandShouldBeRight() {
        HelpCommand underTest = new HelpCommand();

        String actualCommand = underTest.command();
        String expectedCommand = "/help";

        Assertions.assertEquals(expectedCommand, actualCommand);
    }

    @Test
    void descriptionShouldBeRight() {
        HelpCommand underTest = new HelpCommand();

        String actualDescription = underTest.description();
        String expectedDescription = "Show all available commands";

        Assertions.assertEquals(expectedDescription, actualDescription);
    }

}
