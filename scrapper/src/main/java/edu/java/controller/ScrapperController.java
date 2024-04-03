package edu.java.controller;

import edu.java.configuration.RateConfiguration;
import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.controller.dto.response.LinkResponse;
import edu.java.controller.dto.response.ListLinksResponse;
import edu.java.service.chat.ChatService;
import edu.java.service.link.LinkService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scrapper-api/v1.0")
@Slf4j
public class ScrapperController {

    private final LinkService linkService;
    private final ChatService chatService;
    private final Bucket bucket;

    public ScrapperController(LinkService linkService, ChatService chatService, RateConfiguration rateConfiguration) {
        this.linkService = linkService;
        this.chatService = chatService;
        Bandwidth limit = Bandwidth.classic(
            rateConfiguration.limit(),
            Refill.intervally(rateConfiguration.refill(), Duration.ofMinutes(1))
        );
        this.bucket = Bucket.builder()
            .addLimit(limit)
            .build();
    }

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> registerChat(
        @PathVariable @Min(1) Long id
    ) {
        if (bucket.tryConsume(1)) {
            chatService.registerChat(id);
            log.info("Чат зарегистрирован");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> deleteChat(
        @PathVariable @Min(1) Long id
    ) {
        if (bucket.tryConsume(1)) {
            chatService.deleteChat(id);
            log.info("Чат успешно удален");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @GetMapping("/links/{id}")
    public ResponseEntity<ListLinksResponse> getLinks(
        @PathVariable("id") @Min(1) Long chatId
    ) {
        if (bucket.tryConsume(1)) {
            log.info("Ссылки успешно получены");
            return new ResponseEntity<>(linkService.listAll(chatId), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @PostMapping("/links/{id}")
    public ResponseEntity<LinkResponse> addLink(
        @PathVariable("id") @Min(1) Long chatId,
        @RequestBody @Valid AddLinkRequest addLinkRequest
    ) {
        if (bucket.tryConsume(1)) {
            log.info("Ссылка успешно добавлена");
            return new ResponseEntity<>(
                LinkResponse.linkDtoToLinkResponse(linkService.addLink(chatId, addLinkRequest)),
                HttpStatus.OK
            );
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @DeleteMapping("/links/{id}")
    public ResponseEntity<LinkResponse> deleteLink(
        @PathVariable("id") @Min(1) Long chatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        if (bucket.tryConsume(1)) {
            log.info("Ссылка успешно удалена");
            return new ResponseEntity<>(
                LinkResponse.linkDtoToLinkResponse(linkService.removeLink(chatId, removeLinkRequest)),
                HttpStatus.OK
            );
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
