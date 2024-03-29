package edu.java.scrapper.service.chat;

import edu.java.domain.chat.JdbcChatDao;
import edu.java.domain.chat_link.JdbcChatLinkDao;
import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.exception.ChatAlreadyExistException;
import edu.java.exception.ChatNotFoundException;
import edu.java.scrapper.TestData;
import edu.java.service.chat.JdbcChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JdbcChatServiceTest {
    @Mock
    JdbcChatDao jdbcChatDao;
    @Mock
    JdbcChatLinkDao jdbcChatLinkDao;
    @InjectMocks
    JdbcChatService underTest;

    @Test
    void registerNewChatShouldAddChat() {
        final Long testChatId = 1L;
        when(jdbcChatDao.find(anyLong())).thenReturn(Optional.empty());
        underTest.registerChat(testChatId);
        verify(jdbcChatDao, times(1)).add(testChatId);
    }

    @Test
    void registerAlreadyExistChatShouldThrowException() {
        final Long testChatId = 1L;
        when(jdbcChatDao.find(anyLong())).thenReturn(Optional.of(new Chat(testChatId)));
        assertThrows(ChatAlreadyExistException.class, () -> underTest.registerChat(testChatId));
    }

    @Test
    void removingExistingChatShouldRemove() {
        final Long testChatId = 1L;
        when(jdbcChatDao.find(anyLong())).thenReturn(Optional.of(new Chat(testChatId)));
        underTest.deleteChat(testChatId);
        verify(jdbcChatDao, times(1)).remove(testChatId);
    }

    @Test
    void removingAbsentChatShouldReturnException() {
        final Long testChatId = 1L;
        when(jdbcChatDao.find(anyLong())).thenReturn(Optional.empty());
        assertThrows(ChatNotFoundException.class, () -> underTest.deleteChat(testChatId));
    }

    @Test
    void findChatsByLink() {
        final Link testLink = TestData.testLinkDtoFirst();
        when(jdbcChatLinkDao.findChatsByLink(testLink)).thenReturn(List.of(1L, 2L));
        List<Long> actualResult = underTest.findChatsByLink(testLink);
        List<Long> expectedTest = List.of(1L, 2L);
        assertEquals(expectedTest, actualResult);
    }
}
