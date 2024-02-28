package edu.java.scrapper.service;

import edu.java.controller.dto.LinkResponse;
import edu.java.controller.dto.RemoveLinkRequest;
import edu.java.exception.ChatAlreadyExistException;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkAlreadyRegisteredInChatException;
import edu.java.exception.LinkNotFoundByUrlException;
import edu.java.repository.ScrapperRepository;
import edu.java.scrapper.TestData;
import edu.java.service.ScrapperService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScrapperServiceTest {

    @Mock
    private ScrapperRepository scrapperRepository;
    @InjectMocks
    private ScrapperService underTest;

    @Test
    void saveByChatId_chatIsAlreadyExist() {
        when(scrapperRepository.isChatAlreadyExist(anyLong())).thenReturn(true);
        assertThrows(ChatAlreadyExistException.class, () -> underTest.saveChatById(1L));
    }

    @Test
    void saveByChatId_chatIsNotExist() {
        final Long tgChatId = 1L;
        when(scrapperRepository.isChatAlreadyExist(anyLong())).thenReturn(false);
        underTest.saveChatById(tgChatId);
        verify(scrapperRepository, times(1)).saveByChatId(tgChatId);
    }

    @Test
    void deleteChatById_chatIsExist() {
        final Long tgChatId = 1L;
        when(scrapperRepository.isChatAlreadyExist(anyLong())).thenReturn(true);
        underTest.deleteChatById(tgChatId);
        verify(scrapperRepository, times(1)).deleteChatByChatId(tgChatId);
    }

    @Test
    void deleteChatById_chatIsNotExist() {
        when(scrapperRepository.isChatAlreadyExist(anyLong())).thenReturn(false);
        assertThrows(ChatNotFoundException.class, () -> underTest.deleteChatById(TestData.testThChatId()));
    }

    @Test
    void getLinksByChatId_chatIsNotExist() {
        when(scrapperRepository.isChatAlreadyExist(anyLong())).thenReturn(false);
        assertThrows(ChatNotFoundException.class, () -> underTest.getLinksByChatId(TestData.testThChatId()));
    }

    @Test
    void getLinksByChatId_chatIsExist_noLinks() {
        when(scrapperRepository.isChatAlreadyExist(anyLong())).thenReturn(true);
        when(scrapperRepository.getLinksByChatId(anyLong())).thenReturn(TestData.emptyListResponse());

        var expectedListLinksResponse = TestData.emptyListLinksResponse();
        var actualListLinksResponse = underTest.getLinksByChatId(TestData.testThChatId());

        assertEquals(expectedListLinksResponse, actualListLinksResponse);
    }

    @Test
    void getLinksByChatId_chatIsExist_linksExist() {
        when(scrapperRepository.isChatAlreadyExist(anyLong())).thenReturn(true);
        when(scrapperRepository.getLinksByChatId(anyLong())).thenReturn(TestData.listResponseWithLinks());

        var expectedListLinksResponse = TestData.listLinksResponseWithLinks();
        var actualListLinksResponse = underTest.getLinksByChatId(TestData.testThChatId());

        assertEquals(expectedListLinksResponse, actualListLinksResponse);
    }

    @Test
    void addLinkByChatId_chatIsNotExist() {
        when(scrapperRepository.isChatAlreadyExist(anyLong())).thenReturn(false);
        assertThrows(
            ChatNotFoundException.class,
            () -> underTest.addLinkByChatId(TestData.testThChatId(), TestData.testAddLinkRequest())
        );
    }

    @Test
    void addLinkByChatId_chatIsExist_linkIsAlreadyExist() {
        when(scrapperRepository.isChatAlreadyExist(anyLong())).thenReturn(true);
        when(scrapperRepository.getLinksByChatId(anyLong())).thenReturn(TestData.listResponseWithLinks());
        assertThrows(LinkAlreadyRegisteredInChatException.class, () -> underTest.addLinkByChatId(
            TestData.testThChatId(),
            TestData.testAddLinkRequest()
        ));
    }

    @Test
    void addLinkByChatId_chatIsExist_linkIsNotExist() {
        when(scrapperRepository.isChatAlreadyExist(anyLong())).thenReturn(true);
        when(scrapperRepository.getLinksByChatId(anyLong())).thenReturn(TestData.emptyListResponse());

        underTest.addLinkByChatId(TestData.testThChatId(), TestData.testAddLinkRequest());
        verify(scrapperRepository, times(1)).addLinkByChatId(
            TestData.testThChatId(),
            TestData.testAddLinkRequest()
        );
    }

    @Test
    void deleteLink_chatIsNotExist() {
        when(scrapperRepository.isChatAlreadyExist(anyLong())).thenReturn(false);
        assertThrows(
            ChatNotFoundException.class,
            () -> underTest.deleteLink(TestData.testThChatId(), TestData.testRemoveLinkRequest())
        );
    }

    @Test
    void deleteLink_chatIsExist_linkNotFound() {
        when(scrapperRepository.isChatAlreadyExist(anyLong())).thenReturn(true);
        when(scrapperRepository.getLinksByChatId(anyLong())).thenReturn(TestData.emptyListResponse());
        assertThrows(
            LinkNotFoundByUrlException.class,
            () -> underTest.deleteLink(TestData.testThChatId(), TestData.testRemoveLinkRequest())
        );
    }

    @Test
    void deleteLink_chatIsExist_linkIsFound() {
        final RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest("google.com");
        final Long tgChatID = 1L;

        when(scrapperRepository.isChatAlreadyExist(anyLong())).thenReturn(true);
        when(scrapperRepository.getLinksByChatId(anyLong())).thenReturn(TestData.listResponseWithLinks());
        when(scrapperRepository.deleteLink(
            TestData.testThChatId(),
            TestData.testRemoveLinkRequest()
        )).thenReturn(new LinkResponse(TestData.testThChatId(), TestData.testLinkUrl()));

        var expectedDeletedLink = new LinkResponse(TestData.testThChatId(), TestData.testLinkUrl());
        var actualDeletedLink = underTest.deleteLink(tgChatID, removeLinkRequest);

        assertEquals(expectedDeletedLink, actualDeletedLink);

    }

}
