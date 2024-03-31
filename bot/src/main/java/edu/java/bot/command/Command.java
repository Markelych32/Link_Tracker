package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.regex.Pattern;

public interface Command {
    String command();

    String description();

    SendMessage handle(Update update);

    default boolean supports(Update update) {
        Pattern pattern = Pattern.compile("^%s$".formatted(command()));
        return pattern.matcher(update.message().text().split(" ")[0]).matches();
    }

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
