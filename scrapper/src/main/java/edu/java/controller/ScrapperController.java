package edu.java.controller;

import edu.java.controller.dto.ListLinksResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scrapper-api/v1.0")
@Slf4j
public class ScrapperController {

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> registerChat(
        @PathVariable Long id
    ) {
        log.info("Чат зарегистрирован");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> deleteChat(
        @PathVariable Long id
    ) {
        log.info("Чат успешно удален");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getLinks(
        @RequestHeader("Tg-Chat-Id") Long chatId
    ) {
        log.info("Ссылки успешно получены");
        return new ResponseEntity<>(new ListLinksResponse(), HttpStatus.OK);
    }

}
