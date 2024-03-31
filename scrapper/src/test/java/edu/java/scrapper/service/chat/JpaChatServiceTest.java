package edu.java.scrapper.service.chat;

import edu.java.domain.chat.ChatEntity;
import edu.java.domain.dto.jdbc.Link;
import edu.java.domain.link.LinkEntity;
import edu.java.exception.ChatAlreadyExistException;
import edu.java.exception.ChatNotFoundException;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import edu.java.scrapper.TestData;
import edu.java.service.chat.JpaChatService;
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
public class JpaChatServiceTest {

    @Mock
    ChatRepository chatRepository;
    @Mock
    LinkRepository linkRepository;
    @InjectMocks
    JpaChatService underTest;

    @Test
    void registerNewChatShouldSaveChat() {
        final ChatEntity chatEntity = TestData.testChatEntity1();
        when(chatRepository.existsById(anyLong())).thenReturn(false);
        underTest.registerChat(chatEntity.getId());
        verify(chatRepository, times(1)).save(chatEntity);
    }

    @Test
    void registerExistingChatShouldThrowException() {
        final ChatEntity chatEntity = TestData.testChatEntity1();
        when(chatRepository.existsById(anyLong())).thenReturn(true);
        assertThrows(ChatAlreadyExistException.class, () -> underTest.registerChat(chatEntity.getId()));
    }

    @Test
    void deleteExistChatShouldDeleteChat() {
        final ChatEntity chatEntity = TestData.testChatEntity1();
        when(chatRepository.findById(anyLong())).thenReturn(Optional.of(chatEntity));
        underTest.deleteChat(chatEntity.getId());
        verify(chatRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteAbsentChatShouldThrowException() {
        final ChatEntity chatEntity = TestData.testChatEntity1();
        when(chatRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ChatNotFoundException.class, () -> underTest.deleteChat(chatEntity.getId()));
    }

    @Test
    void findChatsByLink() {
        final ChatEntity chatEntity1 = TestData.testChatEntity1();
        final ChatEntity chatEntity2 = TestData.testChatEntity2();
        final Link link = TestData.testLinkDtoFirst();
        link.setId(1L);
        LinkEntity linkEntity = TestData.testLinkEntity1();
        linkEntity.setChats(List.of(chatEntity1, chatEntity2));
        when(linkRepository.findById(anyLong())).thenReturn(Optional.of(linkEntity));
        List<Long> expectedResult = List.of(chatEntity1.getId(), chatEntity2.getId());
        List<Long> actualResult = underTest.findChatsByLink(link);
        assertEquals(expectedResult, actualResult);
    }
}
