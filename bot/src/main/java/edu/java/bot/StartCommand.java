package edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.User;
import edu.java.bot.model.UserState;
import edu.java.bot.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class StartCommand implements Command {

    private static final String COMMAND = "/start";
    private static final String DESCRIPTION = "Register new User";
    private static final String MESSAGE_TO_GET_NAME = """
        *Welcome to out Check Bot!*
        Please, enter your name.""";
    private static final String CONGRATULATION_MESSAGE = """
        Our Congratulations *%s*!
        You have been registered.
        Use command */help* to know what Bot can do.
        """;

    private final UserService userService;

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public boolean supports(Update update) {
        long chatId = update.message().chat().id();
        Optional<User> user = userService.findByChatId(chatId);
        return user.isEmpty() || user.get().getState() == UserState.REGISTRATION;
    }

    @Override
    public SendMessage handle(Update update) {
        String originalMessage = update.message().text();
        Long chatId = update.message().chat().id();

        if (userService.findByChatId(chatId).isEmpty()) {
            User newUser = new User();
            userService.save(newUser, chatId, UserState.REGISTRATION);
            return new SendMessage(chatId, MESSAGE_TO_GET_NAME).parseMode(ParseMode.Markdown);
        }

        User user = userService.findByChatId(chatId).get();
        userService.save(user, originalMessage, UserState.NEUTRAL);
        return new SendMessage(chatId, CONGRATULATION_MESSAGE.formatted(update.message().text()))
            .parseMode(ParseMode.Markdown);
    }
}
