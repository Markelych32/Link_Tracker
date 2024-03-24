package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controller.dto.request.AddLinkRequest;
import edu.java.bot.controller.dto.response.ApiErrorResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {
    private final ScrapperClient scrapperClient;

    private static final String COMMAND = "/track";
    private static final String DESCRIPTION = "Start track the link";
    private static final String MESSAGE_LINK_IS_BEING_TRACKED_NOW = "The Link is now being tracked.";
    private static final String MESSAGE_LINK_WAS_ALREADY_TRACKED = "Sorry, the link was already tracked.";

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
        final String originalMessage = update.message().text();
        Long tgChatId = update.message().chat().id();
        try {
            scrapperClient.addLink(tgChatId, new AddLinkRequest(originalMessage.split(" ")[1]));
        } catch (WebClientResponseException ex) {
            return new SendMessage(
                tgChatId,
                Objects.requireNonNull(ex.getResponseBodyAs(ApiErrorResponse.class)).getDescription()
            );
        }
        return new SendMessage(
            tgChatId, MESSAGE_LINK_IS_BEING_TRACKED_NOW
        );
    }
}
