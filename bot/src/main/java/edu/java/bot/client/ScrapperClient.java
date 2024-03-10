package edu.java.bot.client;

import edu.java.bot.configuration.ClientConfig;
import edu.java.bot.controller.dto.request.AddLinkRequest;
import edu.java.bot.controller.dto.request.RemoveLinkRequest;
import edu.java.bot.controller.dto.response.LinkResponse;
import edu.java.bot.controller.dto.response.ListLinksResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ScrapperClient {

    private static final String GENERAL_PATH = "scrapper-api/";
    private static final String SCRAPPER_API_V_1_0_TG_CHAT_ID = "tg-chat/{id}";
    private static final String SCRAPPER_API_V_1_0_LINKS = "/links";
    private static final String TG_CHAT_ID_IN_HEADER = "Tg-Chat-Id";

    private final WebClient webClient;
    private final ClientConfig clientConfig;

    @Autowired
    public ScrapperClient(WebClient.Builder webClientBuilder, ClientConfig clientConfig) {
        webClient = webClientBuilder.baseUrl(clientConfig.baseUrl()).build();
        this.clientConfig = clientConfig;
    }

    public ScrapperClient(WebClient.Builder webClientBuilder, ClientConfig clientConfig, String baseUrl) {
        webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.clientConfig = clientConfig;
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
