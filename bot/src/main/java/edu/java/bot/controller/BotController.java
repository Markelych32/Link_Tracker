package edu.java.bot.controller;

import edu.java.bot.controller.dto.request.LinkUpdate;
import edu.java.bot.processor.BotMessageSender;
import edu.java.bot.service.RateLimitService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BotController {

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";

    private final BotMessageSender botMessageSender;
    private final RateLimitService rateLimitService;

    @PostMapping("/updates")
    public ResponseEntity<Void> sendUpdate(
        @Valid @RequestBody LinkUpdate linkUpdate,
        HttpServletRequest request
    ) {
        final String ip = Optional.ofNullable(request.getHeader(X_FORWARDED_FOR))
            .orElseGet(request::getRemoteAddr);
        Bucket bucket = rateLimitService.catchBucket(ip);
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
