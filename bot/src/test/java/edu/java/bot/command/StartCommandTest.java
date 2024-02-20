package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.StartCommand;
import edu.java.bot.model.User;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import edu.java.bot.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;

public class StartCommandTest {
    private static UserService userService;
    private static Update update;
    private static Message message;
    private static Chat chat;

    @BeforeAll
    public static void mockInit() {
        userService = mock(UserService.class);
        update = mock(Update.class);
        message = mock(Message.class);
        chat = mock(Chat.class);
    }

    @Test
    public void registrationNewUserShouldAskNameAndCongratulate() {
        User user = new User();
        user.setChatId(13L);
        StartCommand startCommand = new StartCommand(userService);

        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(13L);

        String actualResponseMessageForRegistration =
            startCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessageForRegistration = """
            *Welcome to out Check Bot!*
            Please, enter your name.""";

        when(userService.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(user));

        String actualResponseMessageCongratulation = startCommand.handle(update).getParameters().get("text").toString();
        String expectedResponseMessageCongratulation = """
            Our Congratulations *%s*!
            You have been registered.
            Use command */help* to know what Bot can do.
            """.formatted(update.message().text());

        Assertions.assertEquals(actualResponseMessageForRegistration, expectedResponseMessageForRegistration);
        Assertions.assertEquals(actualResponseMessageCongratulation, expectedResponseMessageCongratulation);
    }

    @Test
    public void commandOfStartCommandShouldBeRight() {
        StartCommand startCommand = new StartCommand(userService);

        String actualCommand = startCommand.command();
        String expectedCommand = "/start";

        Assertions.assertEquals(actualCommand, expectedCommand);
    }

    @Test
    public void descriptionShouldBeRight() {
        StartCommand startCommand = new StartCommand(userService);

        String actualDescription = startCommand.description();
        String expectedDescription = "Register new User";

        Assertions.assertEquals(actualDescription, expectedDescription);
    }
}
