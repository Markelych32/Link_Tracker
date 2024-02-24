package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Link;
import edu.java.bot.model.User;
import edu.java.bot.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {

    private static final String COMMAND = "/list";
    private static final String DESCRIPTION = "Show all tracked links";
    private static final String MESSAGE_TO_REGISTER = "Please, register! Use command */start*.";
    private static final String MESSAGE_ABOUT_TRACKED_LINKS = "*Tracked Links*:\n\n";
    private static final String MESSAGE_ABOUT_NO_TRACKED_LINKS = """
        Ops... There are no tracked links.
        You can add them using */track* command.
        """;

    private final UserService userService;

    private String getListOfTrackedLinks(long chatId) {
        Optional<User> user = userService.findByChatId(chatId);
        if (user.isEmpty()) {
            return MESSAGE_TO_REGISTER;
        }

        List<Link> listOfLinks = user.get().getLinks();
        if (listOfLinks.isEmpty()) {
            return MESSAGE_ABOUT_NO_TRACKED_LINKS;
        }
        String trackedUrls = user.get().getLinks().stream()
            .map(Link::getUrl)
            .collect(Collectors.joining("\n"));
        return MESSAGE_ABOUT_TRACKED_LINKS + trackedUrls;

    }

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
        Optional<User> user = userService.findByChatId(update.message().chat().id());

        return update.message().text().equals(command())
               && user.isPresent();

    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        String message = getListOfTrackedLinks(chatId);
        return new SendMessage(chatId, message).parseMode(ParseMode.Markdown);
    }
}
