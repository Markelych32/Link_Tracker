package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Link;
import edu.java.bot.model.User;
import edu.java.bot.model.UserState;
import java.util.Objects;
import java.util.Optional;
import edu.java.bot.service.LinkService;
import edu.java.bot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UntrackCommand implements Command {

    private static final String COMMAND = "/untrack";
    private static final String DESCRIPTION = "Stop tracking link";
    private static final String MESSAGE_LINK_WAS_NOT_TRACKED = "This link was not tracked.";
    private static final String MESSAGE_NAME_LINK_TO_UNTRACK = "Please, enter the link you want to untrack.";
    private static final String MESSAGE_LINK_IS_NOT_BEING_TRACKED_ANY_MORE = "The link is no longer tracked.";

    private final UserService userService;
    private final LinkService linkService;

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
        Long chatId = update.message().chat().id();
        Optional<User> user = userService.findByChatId(chatId);
        return user.isPresent() &&
               (update.message().text().equals(command()) && user.get().getState() == UserState.NEUTRAL ||
                user.get().getState() == UserState.UNTRACK);
    }

    @Override
    public SendMessage handle(Update update) {
        String message = update.message().text();
        Long chatId = update.message().chat().id();
        User user = userService.findByChatId(chatId).get();

        if (user.getState() != UserState.UNTRACK) {
            userService.save(user, UserState.UNTRACK);
            return new SendMessage(chatId, MESSAGE_NAME_LINK_TO_UNTRACK);
        }


        Optional<Link> link = linkService.findByUrl(message);

        if (link.isEmpty()
            || !userService.wasLinkTracked(user, link.get())) {
            userService.save(user, UserState.NEUTRAL);
            return new SendMessage(chatId, MESSAGE_LINK_WAS_NOT_TRACKED);
        }

        user.getLinks().removeIf(l -> Objects.equals(l.getUrl(), link.get().getUrl()));
        userService.save(user, UserState.NEUTRAL);
        return new SendMessage(chatId, MESSAGE_LINK_IS_NOT_BEING_TRACKED_ANY_MORE);

    }
}
