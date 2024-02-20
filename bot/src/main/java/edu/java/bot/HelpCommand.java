package edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {

    private final UserService userService;

    private static final String COMMAND = "/help";
    private static final String DESCRIPTION = "Show all available commands";
    private static final String LIST_OF_COMMANDS = """
        * Доступные команды*:

        */help* - Вывести список доступных команд
        */track* - Начать отслеживание обновлений по ссылке
        */untrack* - Прекратить отслеживание по ссылке
        */list* - Показать список отслеживаемых ссылок
        """;

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
        String messageText = update.message().text();
        long chatId = update.message().chat().id();
        return messageText.equals(command()) && userService.findByChatId(chatId).isPresent();
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), LIST_OF_COMMANDS)
            .parseMode(ParseMode.Markdown);
    }
}
