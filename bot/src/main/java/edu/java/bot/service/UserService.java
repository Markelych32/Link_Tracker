package edu.java.bot.service;

import edu.java.bot.model.Link;
import edu.java.bot.model.User;
import edu.java.bot.model.UserState;
import edu.java.bot.repository.UserRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LinkService linkService;

    @Autowired
    public UserService(UserRepository userRepository, LinkService linkService) {
        this.userRepository = userRepository;
        this.linkService = linkService;
    }

    public Optional<User> findByChatId(long chatId) {
        return userRepository.findByChatId(chatId);
    }

    public boolean wasLinkTracked(User user, Link link) {
        return user.getLinks().stream()
            .anyMatch(userlink -> Objects.equals(userlink.getUrl(), link.getUrl()));
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void save(User user, UserState userState) {
        user.setState(userState);
        save(user);
    }

    public void save(User user, long chatId, UserState userState) {
        user.setChatId(chatId);
        user.setState(userState);
        save(user);
    }

    public void save(User user, String name, UserState userState) {
        user.setName(name);
        user.setState(userState);
        save(user);
    }

    public void trackLink(User user, Link link) {
        user.getLinks().add(link);
        link.getUsers().add(user);
        save(user);
        linkService.save(link);
    }

}
