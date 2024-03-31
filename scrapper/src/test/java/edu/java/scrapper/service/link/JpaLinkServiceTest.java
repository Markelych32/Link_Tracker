package edu.java.scrapper.service.link;

import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.domain.chat.ChatEntity;
import edu.java.domain.link.LinkEntity;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkAlreadyRegisteredInChatException;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import edu.java.scrapper.TestData;
import edu.java.service.link.JpaLinkService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JpaLinkServiceTest {
    @Mock
    LinkRepository linkRepository;
    @Mock
    ChatRepository chatRepository;
    @InjectMocks
    JpaLinkService underTest;

    @Test
    void addLinkWithNotActualTgChatShouldThrowException() {
        final Long tgChatId = 1L;
        final AddLinkRequest addLinkRequest = TestData.testAddLinkRequest();
        when(chatRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ChatNotFoundException.class, () -> underTest.addLink(tgChatId, addLinkRequest));
    }

    @Test
    void addAlreadyExistLinkInChatShouldThrowException() {
        Long tgChatId = 1L;
        AddLinkRequest addLinkRequest = TestData.testAddLinkRequest();
        LinkEntity linkEntity = new LinkEntity();
        ChatEntity chatEntity = TestData.testChatEntity2();
        linkEntity.setUrl("google.com");
        chatEntity.getLinks().add(linkEntity);
        when(chatRepository.findById(anyLong())).thenReturn(Optional.of(chatEntity));
        assertThrows(LinkAlreadyRegisteredInChatException.class, () -> underTest.addLink(tgChatId, addLinkRequest));
    }

    @Test
    void addAbsoluteNewLink() {
        Long tgChatId = 1L;
        AddLinkRequest addLinkRequest = TestData.testAddLinkRequest();
        ChatEntity chatEntity = TestData.testChatEntity1();
        when(chatRepository.findById(anyLong())).thenReturn(Optional.of(chatEntity));
        when(linkRepository.findByUrl(anyString())).thenReturn(Optional.empty());
        when(linkRepository.save(any(LinkEntity.class))).thenReturn(TestData.testLinkEntity2());
        underTest.addLink(tgChatId, addLinkRequest);
        verify(linkRepository, times(1)).save(any(LinkEntity.class));
    }

    @Test
    void addAlreadyExistLinkBotInAnotherChat() {
        Long tgChatId = 1L;
        AddLinkRequest addLinkRequest = TestData.testAddLinkRequest();
        LinkEntity linkEntity = TestData.testLinkEntity2();
        ChatEntity chatEntity = TestData.testChatEntity1();
        when(chatRepository.findById(anyLong())).thenReturn(Optional.of(chatEntity));
        when(linkRepository.findByUrl(anyString())).thenReturn(Optional.of(linkEntity));
        when(linkRepository.save(any(LinkEntity.class))).thenReturn(linkEntity);
        underTest.addLink(tgChatId, addLinkRequest);
        verify(linkRepository, times(1)).save(any(LinkEntity.class));
    }
}
