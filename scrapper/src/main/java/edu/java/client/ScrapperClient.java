package edu.java.client;

import edu.java.controller.dto.AddLinkRequest;
import edu.java.controller.dto.LinkResponse;
import edu.java.controller.dto.ListLinksResponse;
import edu.java.controller.dto.RemoveLinkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ScrapperClient {
    private static final String BASE_URL = "http://localhost:8080";
    private static final String SCRAPPER_API_V_1_0_TG_CHAT_ID = "scrapper-api/tg-chat/{id}";
    private static final String SCRAPPER_API_V_1_0_LINKS = "scrapper-api/links";
    private static final String TG_CHAT_ID_IN_HEADER = "Tg-Chat-Id";

    private final WebClient webClient;

    @Autowired
    public ScrapperClient(WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public ScrapperClient(WebClient.Builder webClientBuilder, String baseUrl) {
        webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public void registerChat(Long tgChatId) {
        webClient.post()
            .uri(SCRAPPER_API_V_1_0_TG_CHAT_ID)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public void deleteChat(Long tgChatId) {
        webClient.delete()
            .uri(SCRAPPER_API_V_1_0_TG_CHAT_ID)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public ListLinksResponse getLinks(Long tgChatId) {
        return webClient.get()
            .uri(SCRAPPER_API_V_1_0_LINKS)
            .header(TG_CHAT_ID_IN_HEADER, tgChatId.toString())
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse addLink(Long tgChatId, AddLinkRequest addLinkRequest) {
        return webClient.post()
            .uri(SCRAPPER_API_V_1_0_LINKS)
            .header(TG_CHAT_ID_IN_HEADER, tgChatId.toString())
            .bodyValue(addLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse deleteLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        return webClient.method(HttpMethod.DELETE)
            .uri(SCRAPPER_API_V_1_0_LINKS)
            .header(TG_CHAT_ID_IN_HEADER, tgChatId.toString())
            .bodyValue(removeLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }

}
