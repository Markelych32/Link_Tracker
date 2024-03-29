package edu.java.scrapper.controller;

import edu.java.controller.ScrapperController;
import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.controller.dto.response.ListLinksResponse;
import edu.java.domain.dto.Link;
import edu.java.scrapper.TestData;
import edu.java.service.chat.JdbcChatService;
import edu.java.service.link.JdbcLinkService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ScrapperControllerTest {

    private static final String DEFAULT_URL = "/scrapper-api/v1.0";
    private static final String CHAT_URL = "/tg-chat/{id}";
    private static final String LINKS_URL = "/links/{id}";

    @Mock
    private JdbcChatService chatService;
    @Mock
    private JdbcLinkService linkService;
    @InjectMocks
    private ScrapperController underTest;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(underTest).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Chat: Post; Correct Request")
    @SneakyThrows
    void registerChatWithValidParameters() {
        final Long tgChatId = 1L;
        mockMvc.perform(post(DEFAULT_URL + CHAT_URL, tgChatId))
            .andExpect(status().isOk());
        verify(chatService, times(1)).registerChat(tgChatId);
    }

    @Test
    @DisplayName("Chat: Post; Incorrect Request")
    @SneakyThrows
    void registerChatWithNotValidParameters() {
        final Long tgChatId = -1L;
        mockMvc.perform(post(DEFAULT_URL + CHAT_URL, tgChatId))
            .andExpect(status().isBadRequest());
        verify(chatService, times(0)).registerChat(tgChatId);
    }

    @Test
    @DisplayName("Chat: Delete; Correct Request")
    @SneakyThrows
    void deleteChatWithValidParameters() {
        final Long tgChatId = 1L;
        mockMvc.perform(delete(DEFAULT_URL + CHAT_URL, tgChatId)).andExpect(
            status().isOk()
        );
        verify(chatService, times(1)).deleteChat(tgChatId);
    }

    @Test
    @DisplayName("Chat: Delete; Incorrect Request")
    @SneakyThrows
    void deleteChatWithNotValidParameters() {
        final Long tgChatId = -1L;
        mockMvc.perform(delete(DEFAULT_URL + CHAT_URL, tgChatId)).andExpect(status().isBadRequest());
        verify(chatService, times(0)).registerChat(tgChatId);
    }

    @Test
    @DisplayName("Links: Get; Correct Request")
    @SneakyThrows
    void getLinksWithValidParameters() {
        final Long tgChatId = 1L;
        final ListLinksResponse listLinksResponse = TestData.listLinksResponseWithLinks();
        when(linkService.listAll(Mockito.anyLong())).thenReturn(listLinksResponse);
        mockMvc.perform(get(DEFAULT_URL + LINKS_URL, tgChatId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size").value("1"));
        verify(linkService, times(1)).listAll(tgChatId);
    }

    @Test
    @DisplayName("Links: Get; Incorrect Request")
    @SneakyThrows
    void getLinksWithNotValidParameters() {
        final Long tgChatId = -1L;
        mockMvc.perform(get(DEFAULT_URL + LINKS_URL, tgChatId))
            .andExpect(status().isBadRequest());
        verify(linkService, times(0)).listAll(tgChatId);
    }

    @Test
    @DisplayName("Links: Delete; Correct Request")
    @SneakyThrows
    void deleteLinkTest() {
        final Long tgChatId = 1L;
        final RemoveLinkRequest removeLinkRequest = TestData.testRemoveLinkRequest();
        final String addLinkRequestJson = objectMapper.writeValueAsString(removeLinkRequest);
        when(linkService.removeLink(tgChatId, removeLinkRequest)).thenReturn(new Link(
            1L,
            removeLinkRequest.getUrl(),
            null,
            null
        ));
        mockMvc.perform(delete(DEFAULT_URL + LINKS_URL, tgChatId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(addLinkRequestJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.url").value(removeLinkRequest.getUrl()));
        verify(linkService, times(1)).removeLink(tgChatId, removeLinkRequest);
    }

    @Test
    @DisplayName("Links: Post; Correct Request")
    @SneakyThrows
    void addLinkTest() {
        final Long tgChatId = 1L;
        final AddLinkRequest addLinkRequest = TestData.testAddLinkRequest();
        final String addLinkRequestJson = objectMapper.writeValueAsString(addLinkRequest);
        when(linkService.addLink(tgChatId, addLinkRequest)).thenReturn(new Link(
            1L,
            addLinkRequest.getUrl(),
            null,
            null
        ));
        mockMvc.perform(post(DEFAULT_URL + LINKS_URL, tgChatId).
                contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(addLinkRequestJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.url").value(addLinkRequest.getUrl()));
        verify(linkService, times(1)).addLink(tgChatId, addLinkRequest);
    }

}
