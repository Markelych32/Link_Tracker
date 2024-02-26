package edu.java.bot.controller;

import edu.java.bot.controller.dto.LinkUpdate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bot-api/v1.0")
@Slf4j
public class BotController {

    @PostMapping("/updates")
    public ResponseEntity<Void> sendUpdate(
        @Valid @RequestBody LinkUpdate linkUpdate
    ) {
        log.info("Обновление обработано");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
