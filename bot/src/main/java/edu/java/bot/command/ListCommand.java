package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Link;
import edu.java.bot.model.User;
import edu.java.bot.repository.TempRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {

    private final TempRepository repository;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Show all tracked links";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        if (repository.findByChatId(chatId).isEmpty()) {
            String sendMessageText = "Please, register! Use command */start*.";
            return new SendMessage(chatId, sendMessageText)
                .parseMode(ParseMode.Markdown);
        }
        User user = repository.findByChatId(chatId).get();
        // System.out.println(user.getLinks().get(0));
        if (user.getLinks().isEmpty()) {
            String sendMessageText =
                """
                Ops... There are no tracked links.
                You can add them using */track* command.
                """;
            return new SendMessage(chatId, sendMessageText)
                .parseMode(ParseMode.Markdown);
        } else {
            String trackedUrls = repository.getLinks(chatId).stream()
                .map(Link::getUrl)
                .reduce("", (url1, url2) -> url1 + "\n" + url2);
            String sendMessageText =
                """
                *Tracked Links*:
                """;
            return new SendMessage(chatId, sendMessageText + trackedUrls)
                .parseMode(ParseMode.Markdown);
        }
    }
}
