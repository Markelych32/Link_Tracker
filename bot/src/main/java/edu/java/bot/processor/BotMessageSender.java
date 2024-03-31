package edu.java.bot.processor;

import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BotMessageSender {
    private final MainBot mainBot;

    public void sendMessage(List<Long> chats, String message) {
        chats.forEach(chat -> mainBot.execute(new SendMessage(chat, message)));
    }
}
