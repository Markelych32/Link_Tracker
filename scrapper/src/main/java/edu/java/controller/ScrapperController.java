package edu.java.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

}
