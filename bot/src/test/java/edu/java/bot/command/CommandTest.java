package edu.java.bot.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommandTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Test
    void supportedMessageSupportsShouldReturnTrue() {
        Command command = new TestCommand();
        when(update.message()).thenReturn(message);
        when(update.message().text()).thenReturn(TestData.TEST_URL);
        Assertions.assertTrue(command.supports(update));
    }
    @Test
    void notSupportedMessageSupportsShouldReturnFalse() {
        Command command = new TestCommand();
        when(update.message()).thenReturn(message);
        when(update.message().text()).thenReturn(TestData.TEST_DESCRIPTION);
        Assertions.assertFalse(command.supports(update));
    }
}
