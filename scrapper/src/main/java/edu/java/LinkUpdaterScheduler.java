package edu.java;

import edu.java.domain.dto.Link;
import edu.java.exception.LinkNotFoundByUrlException;
import edu.java.service.link.LinkService;
import edu.java.service.update.LinkUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.util.List;

@EnableScheduling
@Component
@RequiredArgsConstructor
@Slf4j
public class LinkUpdaterScheduler {

    private final List<LinkUpdater> linkUpdaters;
    private final LinkService linkService;

    @Value("#{@scheduler.seconds()}")
    private Long seconds;

    @Scheduled(fixedDelayString = "#{@scheduler.interval()}")
    public void update() {
        List<Link> oldLinks = linkService.findOldLinks(seconds);
        for (Link link : oldLinks) {
            LinkUpdater linkUpdate = linkUpdaters.stream()
                .filter(updater -> updater.support(URI.create(link.getUrl())))
                .findFirst()
                .orElseThrow(LinkNotFoundByUrlException::new);
            linkUpdate.update(link);
        }
    }
}
