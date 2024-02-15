package edu.java.bot.utils;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.command.HelpCommand;
import edu.java.bot.command.ListCommand;
import edu.java.bot.command.StartCommand;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.command.UntrackCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MessageProcessor implements UserMessageProcessor {
    private final HelpCommand helpCommand;
    private final ListCommand listCommand;
    private final StartCommand startCommand;
    private final TrackCommand trackCommand;
    private final UntrackCommand untrackCommand;

    @Override
    public List<? extends Command> commands() {
        return List.of(
            helpCommand,
            listCommand,
            startCommand,
            trackCommand,
            untrackCommand
        );
    }

    @Override
    public SendMessage process(Update update) {
        List<? extends Command> rightCommand = commands()
            .stream()
            .filter(command -> command.supports(update))
            .toList();
        if (rightCommand.size() != 1) {
            String sendMessageText =
                """
                    Please, enter a right command
                    or choose from menu using */help* command.
                    """;
            return new SendMessage(update.message().chat().id(), sendMessageText).parseMode(ParseMode.Markdown);
        } else {
            return rightCommand.getFirst().handle(update);
        }
    }
}
