package edu.java.bot.service;

import edu.java.bot.model.Link;
import edu.java.bot.repository.LinkRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LinkService {
    private final LinkRepository linkRepository;

    @Autowired
    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public Optional<Link> findByUrl(String url) {
        return linkRepository.findByUrl(url);
    }

    public void save(Link link) {
        linkRepository.save(link);
    }

    @Transactional
    public void save(Link link, String url) {
        link.setUrl(url);
        save(link);
    }
}
