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
import reactor.util.retry.Retry;

@Component
public class ScrapperClient {

    private static final String GENERAL_PATH = "scrapper-api/v1.0";
    private static final String SCRAPPER_API_TG_CHAT_ID = "/tg-chat/{id}";
    private static final String SCRAPPER_API_LINKS = "/links/{id}";

    private final WebClient webClient;
    private final ClientConfig clientConfig;
    private final Retry retry;

    @Autowired
    public ScrapperClient(WebClient.Builder webClientBuilder, ClientConfig clientConfig, Retry retry) {
        webClient = webClientBuilder.baseUrl(clientConfig.baseUrl()).build();
        this.clientConfig = clientConfig;
        this.retry = retry;
    }

    public ScrapperClient(WebClient.Builder webClientBuilder, ClientConfig clientConfig, String baseUrl, Retry retry) {
        webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.clientConfig = clientConfig;
        this.retry = retry;
    }

    public void registerChat(Long tgChatId) {
        webClient.post()
            .uri(GENERAL_PATH + SCRAPPER_API_TG_CHAT_ID, tgChatId)
            .retrieve()
            .bodyToMono(Void.class)
            .retryWhen(retry)
            .block();
    }

    public void deleteChat(Long tgChatId) {
        webClient.delete()
            .uri(GENERAL_PATH + SCRAPPER_API_TG_CHAT_ID, tgChatId)
            .retrieve()
            .bodyToMono(Void.class)
            .retryWhen(retry)
            .block();
    }

    public ListLinksResponse getLinks(Long tgChatId) {
        return webClient.get()
            .uri(GENERAL_PATH + SCRAPPER_API_LINKS, tgChatId)
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .retryWhen(retry)
            .block();
    }

    public LinkResponse addLink(Long tgChatId, AddLinkRequest addLinkRequest) {
        return webClient.post()
            .uri(GENERAL_PATH + SCRAPPER_API_LINKS, tgChatId)
            .bodyValue(addLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .retryWhen(retry)
            .block();
    }

    public LinkResponse deleteLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        return webClient.method(HttpMethod.DELETE)
            .uri(GENERAL_PATH + SCRAPPER_API_LINKS, tgChatId)
            .bodyValue(removeLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .retryWhen(retry)
            .block();
    }
}
