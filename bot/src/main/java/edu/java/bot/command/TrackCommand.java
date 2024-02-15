package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Link;
import edu.java.bot.model.User;
import edu.java.bot.model.UserState;
import edu.java.bot.repository.TempRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {

    private final TempRepository repository;

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Start track the link";
    }

    @Override
    public boolean supports(Update update) {
        if (update.message().text().equals(command())) {
            return true;
        }
        Long chatId = update.message().chat().id();
        return repository.findByChatId(chatId).isPresent()
               && repository.findByChatId(chatId).get().getState() == UserState.TRACK;
    }

    @Override
    public SendMessage handle(Update update) {
        String originalMessage = update.message().text();
        Long chatId = update.message().chat().id();
        if (repository.findByChatId(chatId).isEmpty()) {
            String sendMessageText = "Please, register! Use command */start*.";
            return new SendMessage(chatId, sendMessageText);
        }
        User user = repository.findByChatId(chatId).get();
        if (user.getState() == UserState.TRACK) {
            if (repository.findByUrl(chatId, originalMessage).isEmpty()) {
                Link link = new Link();
                link.setUrl(originalMessage);
                repository.saveLink(chatId, link);
            }
            Link link = repository.findByUrl(chatId, originalMessage).get();
            user.setState(UserState.NEUTRAL);
            repository.save(user);
            repository.trackLink(user, link);
            String sendMessageText = "The Link is now being tracked.";
            return new SendMessage(chatId, sendMessageText);
        } else {
            user.setState(UserState.TRACK);
            repository.save(user);
            String sendMessageText =
                """
                    Please, enter the* link *you want to track.""";
            return new SendMessage(chatId, sendMessageText)
                .parseMode(ParseMode.Markdown);
        }
    }
}
