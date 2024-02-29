package edu.java.scrapper.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import edu.java.controller.ScrapperController;
import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.response.LinkResponse;
import edu.java.controller.dto.response.ListLinksResponse;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.scrapper.TestData;
import edu.java.service.ScrapperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class ScrapperControllerTest {

    @Mock
    private ScrapperService service;
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
    void registerChat() throws Exception {
        mockMvc.perform(post("/scrapper-api/v1.0/tg-chat/{id}", 1L))
            .andExpect(status().isOk());
        verify(service, times(1)).saveChatById(1L);
    }

    @Test
    void deleteChat() throws Exception {
        mockMvc.perform(delete("/scrapper-api/v1.0/tg-chat/{id}", 1L))
            .andExpect(status().isOk());
        verify(service, times(1)).deleteChatById(1L);
    }

    @Test
    void deleteLink() throws Exception {
        RemoveLinkRequest removeLinkRequest = TestData.testRemoveLinkRequest();
        String removeLinkRequestJson = objectMapper.writeValueAsString(removeLinkRequest);
        when(service.deleteLink(1L, removeLinkRequest)).thenReturn(new LinkResponse(1L, "google.com"));

        mockMvc.perform(delete("/scrapper-api/v1.0/links")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .header("Tg-Chat-Id", 1L)
                .content(removeLinkRequestJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.url").value("google.com"))
            .andExpect(jsonPath("$.id").value(1));

        verify(service, times(1)).deleteLink(1L, removeLinkRequest);
    }

    @Test
    void addLink() throws Exception {
        AddLinkRequest addLinkRequest = TestData.testAddLinkRequest();
        String addLinkRequestJson = objectMapper.writeValueAsString(addLinkRequest);
        when(service.addLinkByChatId(1L, addLinkRequest)).thenReturn(new LinkResponse(1L, "google.com"));
        mockMvc.perform(post("/scrapper-api/v1.0/links")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .header("Tg-Chat-Id", 1L)
                .content(addLinkRequestJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.url").value(addLinkRequest.getUrl()));
    }

    @Test
    void getLinks() throws Exception {
        ListLinksResponse listLinksResponse = TestData.listLinksResponseWithLinks();
        String listLinksResponseJson = objectMapper.writeValueAsString(listLinksResponse);
        when(service.getLinksByChatId(anyLong())).thenReturn(TestData.listLinksResponseWithLinks());
        mockMvc.perform(get("/scrapper-api/v1.0/links")
                .header("Tg-Chat-Id", 1L))
            .andExpect(status().isOk());
        verify(service, times(1)).getLinksByChatId(1L);
    }
}
