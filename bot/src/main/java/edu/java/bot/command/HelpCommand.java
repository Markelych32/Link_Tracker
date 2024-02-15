package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {
    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Show all available commands";
    }

    @Override
    public SendMessage handle(Update update) {
        String sendMessageText =
            """
                * Доступные команды*:

                */start* - Зарегистрировать нового пользователя
                */help* - Вывести список доступных команд
                */track* - Начать отслеживание обновлений по ссылке
                */untrack* - Прекратить отслеживание по ссылке
                */list* - Показать список отслеживаемых ссылок
                """;
        return new SendMessage(update.message().chat().id(), sendMessageText)
            .parseMode(ParseMode.Markdown);
    }
}
