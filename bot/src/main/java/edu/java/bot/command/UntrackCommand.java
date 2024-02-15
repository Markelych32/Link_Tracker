package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Link;
import edu.java.bot.model.User;
import edu.java.bot.model.UserState;
import edu.java.bot.repository.TempRepository;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UntrackCommand implements Command {

    private final TempRepository repository;

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Stop tracking link";
    }

    @Override
    public boolean supports(Update update) {
        if (update.message().text().equals(command())) {
            return true;
        }
        Long chatId = update.message().chat().id();
        return repository.findByChatId(chatId).isPresent()
               && repository.findByChatId(chatId).get().getState() == UserState.UNTRACK;
    }

    @Override
    public SendMessage handle(Update update) {
        String message = update.message().text();
        Long chatId = update.message().chat().id();
        if (repository.findByChatId(chatId).isEmpty()) {
            return new SendMessage(
                chatId,
                "Please, register! Use command */start*."
            ).parseMode(ParseMode.Markdown);
        }
        User user = repository.findByChatId(chatId).get();
        if (user.getState() == UserState.UNTRACK) {
            Optional<Link> link = repository.findByUrl(chatId, message);
            if (link.isEmpty()
                || !repository.wasLinkTracked(user, link.get())) {
                user.setState(UserState.NEUTRAL);
                repository.save(user);
                return new SendMessage(
                    chatId,
                    "This link was not tracked."
                );
            }

            user.getLinks().removeIf(l -> Objects.equals(l.getUrl(), link.get().getUrl()));
            user.setState(UserState.NEUTRAL);
            repository.save(user);
            return new SendMessage(
                chatId,
                "The link is no longer tracked."
            );
        } else {
            user.setState(UserState.UNTRACK);
            repository.save(user);
            return new SendMessage(
                chatId,
                "Please, enter the link you want to untrack."
            );
        }
    }
}
