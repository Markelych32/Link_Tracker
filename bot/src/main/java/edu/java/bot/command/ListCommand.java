package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controller.dto.response.LinkResponse;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {

    private final ScrapperClient scrapperClient;

    private static final String COMMAND = "/list";
    private static final String DESCRIPTION = "Show all tracked links";
    private static final String MESSAGE_ABOUT_TRACKED_LINKS = "*Tracked Links*:\n\n";
    private static final String MESSAGE_ABOUT_NO_TRACKED_LINKS = """
        Ops... There are no tracked links.
        You can add them using */track* command.
        """;

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        Long tgChatId = update.message().chat().id();
        String trackedUrls = scrapperClient.getLinks(tgChatId)
            .getLinks()
            .stream()
            .map(LinkResponse::getUrl)
            .collect(Collectors.joining("\n"));
        if (trackedUrls.isEmpty()) {
            return new SendMessage(tgChatId, MESSAGE_ABOUT_NO_TRACKED_LINKS).parseMode(ParseMode.Markdown);
        } else {
            return new SendMessage(tgChatId, MESSAGE_ABOUT_TRACKED_LINKS + trackedUrls).parseMode(ParseMode.Markdown);
        }
    }
}
