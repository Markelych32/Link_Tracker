package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controller.dto.request.RemoveLinkRequest;
import edu.java.bot.controller.dto.response.ApiErrorResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class UntrackCommand implements Command {

    private final ScrapperClient scrapperClient;

    private static final String COMMAND = "/untrack";
    private static final String DESCRIPTION = "Stop tracking link";
    private static final String MESSAGE_LINK_WAS_NOT_TRACKED = "This link was not tracked.";
    private static final String MESSAGE_NAME_LINK_TO_UNTRACK = "Please, enter the link you want to untrack.";
    private static final String MESSAGE_LINK_IS_NOT_BEING_TRACKED_ANY_MORE = "The link is no longer tracked.";

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
            scrapperClient.deleteLink(tgChatId, new RemoveLinkRequest(originalMessage.split(" ")[1]));
        } catch (WebClientResponseException ex) {
            return new SendMessage(
                tgChatId,
                Objects.requireNonNull(ex.getResponseBodyAs(ApiErrorResponse.class)).getDescription()
            );
        }
        return new SendMessage(tgChatId, MESSAGE_LINK_IS_NOT_BEING_TRACKED_ANY_MORE);
    }
}
