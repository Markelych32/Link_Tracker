package edu.java.bot.controller;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMessageProcessor {

    private final List<Command> commands;

    private final static String MESSAGE_NOT_CORRECT_COMMAND = """
        Please, enter a right command
        or choose from menu using */help* command.
        """;
    private final List<Command> commands() {
        return commands;
    }

    public SendMessage process(Update update) {
        return commands().stream()
            .filter(command -> command.supports(update))
            .findFirst()
            .map(it -> it.handle(update))
            .orElseGet(() -> new SendMessage(update.message().chat().id(), MESSAGE_NOT_CORRECT_COMMAND).parseMode(ParseMode.Markdown));
    }
}
