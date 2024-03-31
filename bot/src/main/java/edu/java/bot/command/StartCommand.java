package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {

    private final ScrapperClient scrapperClient;

    private static final String COMMAND = "/start";
    private static final String DESCRIPTION = "Register new User";
    private static final String CONGRATULATION_MESSAGE = """
        Our Congratulations!
        You have been registered.
        Use command */help* to know what Bot can do.
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
        try {
            scrapperClient.registerChat(tgChatId);
            return new SendMessage(tgChatId, CONGRATULATION_MESSAGE).parseMode(ParseMode.Markdown);
        } catch (WebClientResponseException e) {
            return new SendMessage(
                tgChatId,
                e.getResponseBodyAsString()
            );
        }
    }
}
