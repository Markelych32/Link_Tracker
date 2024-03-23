package edu.java;

import edu.java.domain.dto.jdbc.Link;
import edu.java.exception.LinkNotFoundByUrlException;
import edu.java.service.link.JdbcLinkService;
import edu.java.service.link.LinkService;
import edu.java.service.update.LinkUpdater;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@RequiredArgsConstructor
@Slf4j
public class LinkUpdaterScheduler {

    private final List<LinkUpdater> linkUpdaters;
    private final JdbcLinkService linkService;

    @Value("${app.scheduler.seconds}")
    private int seconds;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}", timeUnit = TimeUnit.SECONDS)
    public void update() {
        List<Link> oldLinks = linkService.findOldLinks(seconds);
        for (Link link : oldLinks) {
            LinkUpdater linkUpdater = linkUpdaters.stream()
                .filter(updater -> updater.support(URI.create(link.getUrl())))
                .findFirst()
                .orElseThrow(LinkNotFoundByUrlException::new);
            linkUpdater.update(link);
        }
    }
}
