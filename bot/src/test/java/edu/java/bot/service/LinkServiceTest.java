package edu.java.bot.service;

import edu.java.bot.model.Link;
import edu.java.bot.repository.LinkRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LinkServiceTest {

    private static final String TEST_URL = "test_url";

    @InjectMocks
    private LinkService linkService;
    @Mock
    private LinkRepository linkRepository;

    @Test
    void findByUrlExistingUrl() {
        Link link = new Link();
        link.setUrl(TEST_URL);

        when(linkRepository.findByUrl(Mockito.anyString())).thenReturn(Optional.of(link));
        Optional<Link> expectedLink = Optional.of(link);
        Optional<Link> actualLink = linkService.findByUrl(TEST_URL);

        Assertions.assertEquals(expectedLink, actualLink);
    }

    @Test
    void findByUrlWhenNoSuchUrl() {
        when(linkRepository.findByUrl(Mockito.anyString())).thenReturn(Optional.empty());

        Optional<Link> expectedLink = Optional.empty();
        Optional<Link> actualLink = linkService.findByUrl(TEST_URL);

        Assertions.assertEquals(Optional.empty(), linkService.findByUrl(TEST_URL));
    }

    @Test
    void savingLinkShouldRepeatOneTime() {
        Link link = new Link();
        link.setUrl(TEST_URL);
        linkService.save(link);
        Mockito.verify(linkRepository, Mockito.times(1)).save(link);
    }
}
