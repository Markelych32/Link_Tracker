package edu.java;

import edu.java.domain.dto.Link;
import edu.java.exception.LinkNotFoundByUrlException;
import edu.java.service.link.LinkService;
import edu.java.service.update.LinkUpdater;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@RequiredArgsConstructor
@Slf4j
public class LinkUpdaterScheduler {

    private final List<LinkUpdater> linkUpdaters;
    private final LinkService linkService;

    //@Value("#{@scheduler.seconds()}")
    private static final Long SECONDS = 60L;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}", timeUnit = TimeUnit.SECONDS)
    public void update() {
        log.info("The Link was Update");
        List<Link> oldLinks = linkService.findOldLinks(SECONDS);
        for (Link link : oldLinks) {
            LinkUpdater linkUpdate = linkUpdaters.stream()
                .filter(updater -> updater.support(URI.create(link.getUrl())))
                .findFirst()
                .orElseThrow(LinkNotFoundByUrlException::new);
            linkUpdate.update(link);
        }
    }
}
