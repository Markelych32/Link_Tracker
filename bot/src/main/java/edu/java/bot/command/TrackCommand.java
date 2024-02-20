package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Link;
import edu.java.bot.model.User;
import edu.java.bot.model.UserState;
import edu.java.bot.service.LinkService;
import edu.java.bot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {

    private final UserService userService;
    private final LinkService linkService;

    private static final String COMMAND = "/track";
    private static final String DESCRIPTION = "Start track the link";
    private static final String MESSAGE_TO_GET_LINK_TO_TRACK = "Please, enter the* link *you want to track.";
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
    public boolean supports(Update update) {
        long chatId = update.message().chat().id();
        Optional<User> user = userService.findByChatId(chatId);
        return user.isPresent()
               && (user.get().getState() == UserState.TRACK || update.message().text().equals(command()));
    }
    @Override
    public SendMessage handle(Update update) {
        String message = update.message().text();
        long chatId = update.message().chat().id();
        User user = userService.findByChatId(chatId).get();

        if (user.getState() != UserState.TRACK) {
            userService.save(user, UserState.TRACK);
            return new SendMessage(chatId, MESSAGE_TO_GET_LINK_TO_TRACK).parseMode(ParseMode.Markdown);
        }

        if (linkService.findByUrl(message).isEmpty()) {
            Link link = new Link();
            linkService.save(link, message);
        }

        Link link = linkService.findByUrl(message).get();
        userService.save(user, UserState.NEUTRAL);

        if (userService.wasLinkTracked(user, link)) {
            return new SendMessage(chatId, MESSAGE_LINK_WAS_ALREADY_TRACKED);
        }

        userService.trackLink(user, link);
        return new SendMessage(chatId, MESSAGE_LINK_IS_BEING_TRACKED_NOW).parseMode(ParseMode.Markdown);
    }
}
