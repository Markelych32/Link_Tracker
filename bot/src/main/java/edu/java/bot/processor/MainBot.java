package edu.java.bot.processor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.command.Command;
import edu.java.bot.configuration.ApplicationConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MainBot implements AutoCloseable, UpdatesListener {
    private final ApplicationConfig applicationConfig;
    private final UserMessageProcessor messageProcessor;
    private TelegramBot telegramBot;
    private final List<Command> commands;

    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        telegramBot.execute(request);
    }

    public int process(List<Update> list) {
        list.forEach(update -> execute(messageProcessor.process(update)));
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @PostConstruct
    public void start() {
        SetMyCommands setMyCommands = new SetMyCommands(
            commands.stream()
                .filter(command -> !command.command().equals("/start"))
                .map(Command::toApiCommand)
                .toArray(BotCommand[]::new));
        telegramBot = new TelegramBot(applicationConfig.telegramToken());
        telegramBot.execute(setMyCommands);
        telegramBot.setUpdatesListener(this);
    }

    @PreDestroy
    public void close() {
        telegramBot.removeGetUpdatesListener();
        telegramBot.shutdown();
    }
}
