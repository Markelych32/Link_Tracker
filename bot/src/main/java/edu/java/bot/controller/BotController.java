package edu.java.bot.controller;

import edu.java.bot.configuration.RateConfiguration;
import edu.java.bot.controller.dto.request.LinkUpdate;
import edu.java.bot.processor.BotMessageSender;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.validation.Valid;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Slf4j
public class BotController {

    private final BotMessageSender botMessageSender;
    private final Bucket bucket;

    @Autowired
    public BotController(BotMessageSender botMessageSender, RateConfiguration rateConfiguration) {
        this.botMessageSender = botMessageSender;
        Bandwidth limit = Bandwidth.classic(
            rateConfiguration.limit(),
            Refill.intervally(rateConfiguration.refill(), Duration.ofMinutes(1))
        );
        this.bucket = Bucket.builder()
            .addLimit(limit)
            .build();
    }

    @PostMapping("/updates")
    public ResponseEntity<Void> sendUpdate(
        @Valid @RequestBody LinkUpdate linkUpdate
    ) {
        if (bucket.tryConsume(1)) {
            botMessageSender.sendMessage(
                linkUpdate.getTgChatIds(),
                linkUpdate.getDescription()
            );
            log.info("Обновление обработано");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
