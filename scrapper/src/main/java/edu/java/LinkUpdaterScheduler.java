package edu.java;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Slf4j
@EnableScheduling
@Component
public class LinkUpdaterScheduler {
    @Scheduled(fixedDelayString = "${app.scheduler.interval}", timeUnit = TimeUnit.SECONDS)
    public void update() {
        log.info("Link was updated!");
    }
}
