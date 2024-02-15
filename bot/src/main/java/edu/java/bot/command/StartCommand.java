package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.User;
import edu.java.bot.model.UserState;
import edu.java.bot.repository.TempRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {
    private final TempRepository repository;

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Register new User";
    }

    @Override
    public boolean supports(Update update) {
        if (update.message().text().equals(command())) {
            return true;
        }
        Long chatId = update.message().chat().id();
        return repository.findByChatId(chatId).isPresent()
               && repository.findByChatId(chatId).get().getState() == UserState.REGISTRATION;
    }

    @Override
    public SendMessage handle(Update update) {
        String originalMessage = update.message().text();
        Long chatId = update.message().chat().id();
        if (repository.findByChatId(chatId).isPresent() && repository.findByChatId(chatId).get().getName() != null) {
            return new SendMessage(chatId, "Sorry, You have been already registered.");
        }
        if (repository.findByChatId(chatId).isEmpty()) {
            User user = new User();
            user.setChatId(chatId);
            repository.save(user);
        }
        User user = repository.findByChatId(chatId).get();
        if (user.getState() == UserState.REGISTRATION) {
            user.setName(originalMessage);
            user.setState(UserState.NEUTRAL);
            repository.save(user);
            String sendMessageText =
                """
                Our Congratulations *%s*!
                You have been registered.
                """.formatted(update.message().text());
            return new SendMessage(chatId, sendMessageText)
                .parseMode(ParseMode.Markdown);
        } else {
            user.setState(UserState.REGISTRATION);
            repository.save(user);
            String sendMessageText = "Please, enter your name.";
            return new SendMessage(chatId, sendMessageText);
        }
    }
}
