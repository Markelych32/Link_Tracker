package edu.java.bot.utils;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.command.HelpCommand;
import edu.java.bot.command.ListCommand;
import edu.java.bot.command.StartCommand;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.command.UntrackCommand;
import edu.java.bot.configuration.ApplicationConfig;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MainBot implements Bot {
    private final ApplicationConfig applicationConfig;
    private final UserMessageProcessor messageProcessor;
    private TelegramBot telegramBot;

    private final HelpCommand helpCommand;
    private final ListCommand listCommand;
    private final StartCommand startCommand;
    private final TrackCommand trackCommand;
    private final UntrackCommand untrackCommand;

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        telegramBot.execute(request);
    }

    @Override
    public int process(List<Update> list) {
        list.forEach(update -> execute(messageProcessor.process(update)));
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void start() {
        SetMyCommands commands = new SetMyCommands(
            helpCommand.toApiCommand(),
            listCommand.toApiCommand(),
            startCommand.toApiCommand(),
            trackCommand.toApiCommand(),
            untrackCommand.toApiCommand()
        );
        telegramBot = new TelegramBot(applicationConfig.telegramToken());
        telegramBot.execute(commands);
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public void close() {
        telegramBot.removeGetUpdatesListener();
    }
}
