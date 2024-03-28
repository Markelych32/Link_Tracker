package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.TestData;
import org.mockito.Mock;

public class TestCommand implements Command {

    @Mock
    private Update update;

    @Override
    public String command() {
        return TestData.TEST_URL;
    }

    @Override
    public String description() {
        return TestData.TEST_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(TestData.TEST_ID, TestData.TEST_MESSAGE);
    }
}
