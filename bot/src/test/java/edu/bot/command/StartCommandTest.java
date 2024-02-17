package edu.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.command.StartCommand;
import edu.java.bot.model.User;
import edu.java.bot.repository.TempRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;

public class StartCommandTest {
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
    public void registrationNewUserShouldAskNameAndCongratulate() {
        User user = new User();
        user.setChatId(13L);
        StartCommand startCommand = new StartCommand(repository);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/start");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(13L);
        Mockito.when(repository.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(user));

        String actualResponseMessageForRegistration =
            startCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessageForRegistration = "Please, enter your name.";
        String actualResponseMessageCongratulation = startCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessageCongratulation = """
            Our Congratulations *%s*!
            You have been registered.
            """.formatted(update.message().text());

        Assertions.assertEquals(actualResponseMessageForRegistration, expectedResponseMessageForRegistration);
        Assertions.assertEquals(actualResponseMessageCongratulation, expectedResponseMessageCongratulation);
    }

    @Test
    public void commandOfStartCommandShouldBeRight() {
        StartCommand startCommand = new StartCommand(repository);

        String actualCommand = startCommand.command();
        String expectedCommand = "/start";

        Assertions.assertEquals(actualCommand, expectedCommand);
    }

    @Test
    public void descriptionShouldBeRight() {
        StartCommand startCommand = new StartCommand(repository);

        String actualDescription = startCommand.description();
        String expectedDescription = "Register new User";

        Assertions.assertEquals(actualDescription, expectedDescription);
    }
}
