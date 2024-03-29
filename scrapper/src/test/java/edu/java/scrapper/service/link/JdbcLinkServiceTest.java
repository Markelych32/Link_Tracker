package edu.java.scrapper.service.link;

import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.controller.dto.response.LinkResponse;
import edu.java.controller.dto.response.ListLinksResponse;
import edu.java.domain.chat.JdbcChatDao;
import edu.java.domain.chat_link.JdbcChatLinkDao;
import edu.java.domain.dto.Chat;
import edu.java.domain.dto.ChatLink;
import edu.java.domain.dto.Link;
import edu.java.domain.link.JdbcLinkDao;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkAlreadyRegisteredInChatException;
import edu.java.exception.LinkNotFoundByUrlException;
import edu.java.scrapper.TestData;
import edu.java.service.link.JdbcLinkService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JdbcLinkServiceTest {
    @Mock
    JdbcLinkDao linkDao;
    @Mock
    JdbcChatDao chatDao;
    @Mock
    JdbcChatLinkDao chatLinkDao;
    @InjectMocks
    JdbcLinkService underTest;

    @Test
    void addLinkWithExistingChatShouldThrowException() {
        when(chatDao.find(anyLong())).thenReturn(Optional.empty());
        assertThrows(
            ChatNotFoundException.class,
            () -> underTest.addLink(1L, TestData.testAddLinkRequest())
        );
    }

    @Test
    void addingExistingLinkInTwoTableShouldThrowException() {
        Long chatId = 1L;
        Chat chat = new Chat(chatId);
        AddLinkRequest addLinkRequest = new AddLinkRequest("test-url.com");
        Link link = new Link(1L, addLinkRequest.getUrl(), OffsetDateTime.now(), OffsetDateTime.now());
        ChatLink chatLink = new ChatLink(1L, 1L);
        when(chatDao.find(chatId)).thenReturn(Optional.of(chat));
        when(linkDao.find(addLinkRequest.getUrl())).thenReturn(Optional.of(link));
        when(chatLinkDao.find(chatId, link.getId())).thenReturn(Optional.of(chatLink));
        assertThrows(LinkAlreadyRegisteredInChatException.class, () -> underTest.addLink(chatId, addLinkRequest));
    }

    @Test
    void addingLinkWhichAbsentInChatLinkTableShouldBeSaved() {
        Long chatId = 1L;
        Chat chat = new Chat(chatId);
        AddLinkRequest addLinkRequest = new AddLinkRequest("test-url.com");
        Link link = new Link(1L, addLinkRequest.getUrl(), OffsetDateTime.now(), OffsetDateTime.now());
        ChatLink chatLink = new ChatLink(1L, 1L);
        when(chatDao.find(chatId)).thenReturn(Optional.of(chat));
        when(linkDao.find(addLinkRequest.getUrl())).thenReturn(Optional.of(link));
        when(chatLinkDao.find(chatId, link.getId())).thenReturn(Optional.empty());
        assertEquals(link, underTest.addLink(chatId, addLinkRequest));
        verify(chatLinkDao, times(1)).add(chatId, link.getId());
    }

    @Test
    void removingLinkFromAbsentChatShouldThrowException() {
        when(chatDao.find(anyLong())).thenReturn(Optional.empty());
        assertThrows(
            ChatNotFoundException.class,
            () -> underTest.removeLink(anyLong(), TestData.testRemoveLinkRequest())
        );
    }

    @Test
    void removingNotExistingLinkShouldThrowException() {
        when(chatDao.find(anyLong())).thenReturn(Optional.of(new Chat(1L)));
        when(linkDao.find(anyString())).thenReturn(Optional.empty());
        assertThrows(
            LinkNotFoundByUrlException.class,
            () -> underTest.removeLink(anyLong(), TestData.testRemoveLinkRequest())
        );
    }

    @Test
    void removeTestFromOneTable() {
        Long chatId = 1L;
        Chat chat = new Chat(chatId);
        Link link = new Link(1L, "test-url.com", OffsetDateTime.now(), OffsetDateTime.now());
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest("test-url.com");
        when(chatDao.find(chatId)).thenReturn(Optional.of(chat));
        when(linkDao.find(removeLinkRequest.getUrl())).thenReturn(Optional.of(link));
        when(chatLinkDao.remove(chatId, link)).thenReturn(true);
        when(chatLinkDao.isLinkPresent(link)).thenReturn(true);
        assertEquals(link, underTest.removeLink(chatId, removeLinkRequest));
    }

    @Test
    void removeTestFromTwoTable() {
        Long chatId = 1L;
        Chat chat = new Chat(chatId);
        Link link = new Link(1L, "test-url.com", OffsetDateTime.now(), OffsetDateTime.now());
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest("test-url.com");
        when(chatDao.find(chatId)).thenReturn(Optional.of(chat));
        when(linkDao.find(removeLinkRequest.getUrl())).thenReturn(Optional.of(link));
        when(chatLinkDao.remove(chatId, link)).thenReturn(true);
        when(chatLinkDao.isLinkPresent(link)).thenReturn(false);
        assertEquals(link, underTest.removeLink(chatId, removeLinkRequest));
        verify(linkDao, times(1)).remove(removeLinkRequest.getUrl());
    }

    @Test
    void updateTest() {
        final Link testLink = TestData.testLinkDtoFirst();
        underTest.update(testLink);
        verify(linkDao, times(1)).update(testLink);
    }

    @Test
    void findOldLinksTest() {
        final Link testLink = new Link(null, "test", OffsetDateTime.MIN, OffsetDateTime.MIN);
        when(linkDao.findAll()).thenReturn(List.of(testLink));
        final List<Link> actualResult = underTest.findOldLinks(1);
        Assertions.assertEquals(List.of(actualResult.get(0)), actualResult);
    }

    @Test
    void listAllWithAbsentChatShouldThrowException() {
        when(chatDao.find(anyLong())).thenReturn(Optional.empty());
        assertThrows(ChatNotFoundException.class, () -> underTest.listAll(anyLong()));
    }

    @Test
    void listAllWithExistingChatShouldReturnListOfLinkResponse() {
        final Long testLinkId = 1L;
        final Link testLink = TestData.testLinkDtoFirst();
        testLink.setId(testLinkId);
        when(chatDao.find(anyLong())).thenReturn(Optional.of(new Chat(1L)));
        when(linkDao.findAllByChat(anyLong())).thenReturn(List.of(testLink));
        List<LinkResponse> linkResponses = List.of(new LinkResponse(
            testLink.getId(),
            testLink.getUrl()
        ));
        ListLinksResponse expectedResult = new ListLinksResponse(linkResponses, 1);
        Assertions.assertEquals(expectedResult, underTest.listAll(anyLong()));
    }
}
