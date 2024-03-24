package edu.java.bot.controller;

import edu.java.bot.controller.dto.request.LinkUpdate;
import edu.java.bot.processor.BotMessageSender;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class BotController {

    private final BotMessageSender botMessageSender;

    @PostMapping("/updates")
    public ResponseEntity<Void> sendUpdate(
        @Valid @RequestBody LinkUpdate linkUpdate
    ) {
        botMessageSender.sendMessage(
            linkUpdate.getTgChatIds(),
            linkUpdate.getDescription()
        );
        log.info("Обновление обработано");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
