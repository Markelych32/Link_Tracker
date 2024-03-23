package edu.java.controller;

import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.controller.dto.response.LinkResponse;
import edu.java.controller.dto.response.ListLinksResponse;
import edu.java.service.chat.JdbcChatService;
import edu.java.service.chat.JpaChatService;
import edu.java.service.link.JdbcLinkService;
import edu.java.service.link.JpaLinkService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Slf4j
public class ScrapperController {

    private final JdbcLinkService jdbcLinkService;
    private final JdbcChatService jdbcChatService;
    private final JpaChatService jpaChatService;
    private final JpaLinkService jpaLinkService;

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> registerChat(
        @PathVariable @Min(1) Long id
    ) {
        //jdbcChatService.registerChat(id);
        jpaChatService.registerChat(id);
        log.info("Чат зарегистрирован");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> deleteChat(
        @PathVariable @Min(1) Long id
    ) {
        jpaChatService.deleteChat(id);
//        jdbcChatService.deleteChat(id);
        log.info("Чат успешно удален");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/links/{id}")
    public ResponseEntity<ListLinksResponse> getLinks(
        @PathVariable("id") @Min(1) Long chatId
    ) {
        log.info("Ссылки успешно получены");
        //return new ResponseEntity<>(jdbcLinkService.listAll(chatId), HttpStatus.OK);
        return new ResponseEntity<>(jpaLinkService.listAll(chatId), HttpStatus.OK);
    }

    @PostMapping("/links/{id}")
    public ResponseEntity<LinkResponse> addLink(
        @PathVariable("id") @Min(1) Long chatId,
        @RequestBody @Valid AddLinkRequest addLinkRequest
    ) {
        log.info("Ссылка успешно добавлена");
        return new ResponseEntity<>(
            //LinkResponse.linkDtoToLinkResponse(jdbcLinkService.addLink(chatId, addLinkRequest)),
            LinkResponse.linkDtoToLinkResponse(jpaLinkService.addLink(chatId, addLinkRequest)),
            HttpStatus.OK
        );
    }

    @DeleteMapping("/links/{id}")
    public ResponseEntity<LinkResponse> deleteLink(
        @PathVariable("id") @Min(1) Long chatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        log.info("Ссылка успешно удалена");
        return new ResponseEntity<>(
            LinkResponse.linkDtoToLinkResponse(jpaLinkService.removeLink(chatId, removeLinkRequest)),
            //LinkResponse.linkDtoToLinkResponse(jdbcLinkService.removeLink(chatId, removeLinkRequest)),
            HttpStatus.OK
        );
    }
}
