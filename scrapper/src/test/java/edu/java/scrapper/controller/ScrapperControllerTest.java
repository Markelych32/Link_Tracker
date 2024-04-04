package edu.java.scrapper.controller;

import edu.java.controller.ScrapperController;
import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.controller.dto.response.ListLinksResponse;
import edu.java.domain.dto.jdbc.Link;
import edu.java.scrapper.TestData;
import edu.java.service.RateLimitService;
import edu.java.service.chat.ChatService;
import edu.java.service.link.LinkService;
import io.github.bucket4j.Bucket;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(ScrapperController.class)
public class ScrapperControllerTest {

    private static final String DEFAULT_URL = "/scrapper-api/v1.0";
    private static final String CHAT_URL = "/tg-chat/{id}";
    private static final String LINKS_URL = "/links/{id}";

    @MockBean
    private ChatService chatService;
    @MockBean
    private LinkService linkService;
    @MockBean
    private RateLimitService rateLimitService;
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Chat: Post; Correct Request")
    @SneakyThrows
    void registerChatWithValidParameters() {
        Bucket bucket = mock(Bucket.class);
        when(rateLimitService.catchBucket(anyString())).thenReturn(bucket);
        when(bucket.tryConsume(anyLong())).thenReturn(true);
        final Long tgChatId = 1L;
        mockMvc.perform(post(DEFAULT_URL + CHAT_URL, tgChatId)
                .header("X-Forwarded-For", "0.0.0.0"))
            .andExpect(status().isOk());
        verify(chatService, times(1)).registerChat(tgChatId);
    }

    @Test
    @DisplayName("Chat: Post; Incorrect Request")
    @SneakyThrows
    void registerChatWithNotValidParameters() {
        Bucket bucket = mock(Bucket.class);
        when(rateLimitService.catchBucket(anyString())).thenReturn(bucket);
        when(bucket.tryConsume(anyLong())).thenReturn(true);
        final Long tgChatId = -1L;
        mockMvc.perform(post(DEFAULT_URL + CHAT_URL, tgChatId)
                .header("X-Forwarded-For", "0.0.0.0"))
            .andExpect(status().isNotFound());
        verify(chatService, times(0)).registerChat(tgChatId);
    }

    @Test
    @DisplayName("Chat: Delete; Correct Request")
    @SneakyThrows
    void deleteChatWithValidParameters() {
        final Long tgChatId = 1L;
        Bucket bucket = mock(Bucket.class);
        when(rateLimitService.catchBucket(anyString())).thenReturn(bucket);
        when(bucket.tryConsume(anyLong())).thenReturn(true);
        mockMvc.perform(delete(DEFAULT_URL + CHAT_URL, tgChatId)
                .header("X-Forwarded-For", "0.0.0.0"))
            .andExpect(status().isOk());
        verify(chatService, times(1)).deleteChat(tgChatId);
    }

    @Test
    @DisplayName("Chat: Delete; Incorrect Request")
    @SneakyThrows
    void deleteChatWithNotValidParameters() {
        final Long tgChatId = -1L;
        Bucket bucket = mock(Bucket.class);
        when(rateLimitService.catchBucket(anyString())).thenReturn(bucket);
        when(bucket.tryConsume(anyLong())).thenReturn(true);
        mockMvc.perform(delete(DEFAULT_URL + CHAT_URL, tgChatId)
                .header("X-Forwarded-For", "0.0.0.0"))
            .andExpect(status().isNotFound());
        verify(chatService, times(0)).registerChat(tgChatId);
    }

    @Test
    @DisplayName("Links: Get; Correct Request")
    @SneakyThrows
    void getLinksWithValidParameters() {
        final Long tgChatId = 1L;
        final ListLinksResponse listLinksResponse = TestData.listLinksResponseWithLinks();
        Bucket bucket = mock(Bucket.class);
        when(rateLimitService.catchBucket(anyString())).thenReturn(bucket);
        when(bucket.tryConsume(anyLong())).thenReturn(true);
        when(linkService.listAll(anyLong())).thenReturn(listLinksResponse);
        mockMvc.perform(get(DEFAULT_URL + LINKS_URL, tgChatId)
                .header("X-Forwarded-For", "0.0.0.0"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size").value("1"));
        verify(linkService, times(1)).listAll(tgChatId);
    }

    @Test
    @DisplayName("Links: Get; Incorrect Request")
    @SneakyThrows
    void getLinksWithNotValidParameters() {
        final Long tgChatId = -1L;
        Bucket bucket = mock(Bucket.class);
        when(rateLimitService.catchBucket(anyString())).thenReturn(bucket);
        when(bucket.tryConsume(anyLong())).thenReturn(true);
        mockMvc.perform(get(DEFAULT_URL + LINKS_URL, tgChatId)
                .header("X-Forwarded-For", "0.0.0.0"))
            .andExpect(status().isNotFound());
        verify(linkService, times(0)).listAll(tgChatId);
    }

    @Test
    @DisplayName("Links: Delete; Correct Request")
    @SneakyThrows
    void deleteLinkTest() {
        final Long tgChatId = 1L;
        final RemoveLinkRequest removeLinkRequest = TestData.testRemoveLinkRequest();
        final String addLinkRequestJson = objectMapper.writeValueAsString(removeLinkRequest);
        Bucket bucket = mock(Bucket.class);
        when(rateLimitService.catchBucket(anyString())).thenReturn(bucket);
        when(bucket.tryConsume(anyLong())).thenReturn(true);
        when(linkService.removeLink(tgChatId, removeLinkRequest)).thenReturn(new Link(
            1L,
            removeLinkRequest.getUrl(),
            null,
            null
        ));
        mockMvc.perform(delete(DEFAULT_URL + LINKS_URL, tgChatId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(addLinkRequestJson)
                .header("X-Forwarded-For", "0.0.0.0"))
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
        Bucket bucket = mock(Bucket.class);
        when(rateLimitService.catchBucket(anyString())).thenReturn(bucket);
        when(bucket.tryConsume(anyLong())).thenReturn(true);
        when(linkService.addLink(tgChatId, addLinkRequest)).thenReturn(new Link(
            1L,
            addLinkRequest.getUrl(),
            null,
            null
        ));
        mockMvc.perform(post(DEFAULT_URL + LINKS_URL, tgChatId).
                contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(addLinkRequestJson)
                .header("X-Forwarded-For", "0.0.0.0"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.url").value(addLinkRequest.getUrl()));
        verify(linkService, times(1)).addLink(tgChatId, addLinkRequest);
    }
}
