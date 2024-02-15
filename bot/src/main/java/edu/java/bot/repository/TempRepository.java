package edu.java.bot.repository;

import edu.java.bot.model.Link;
import edu.java.bot.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TempRepository {
    private final Map<User, List<Link>> users = new HashMap<>();

    public Optional<User> findByChatId(long chatId) {
        for (User user : users.keySet()) {
            if (user.getChatId() == chatId) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public void save(User user) {
        if (!users.containsKey(user)) {
            users.put(user, new ArrayList<>());
        }
    }

    public Optional<Link> findByUrl(long chatId, String url) {
        if (findByChatId(chatId).isPresent()) {
            User user = findByChatId(chatId).get();
            List<Link> links = users.get(user);
            for (Link link : links) {
                if (link.getUrl().equals(url)) {
                    return Optional.of(link);
                }
            }
        }
        return Optional.empty();
    }

    public List<Link> getLinks(long chatId) {
        if (findByChatId(chatId).isPresent()) {
            return users.get(findByChatId(chatId).get());
        }
        return new ArrayList<>();
    }

    public void saveLink(long chatId, Link link) {
        if (findByChatId(chatId).isPresent()) {
            User user = findByChatId(chatId).get();
            users.get(user).add(link);
        }
    }

    public void trackLink(User user, Link link) {
        if (findByUrl(user.getChatId(), link.getUrl()).isPresent()) {
            Link adedlink = findByUrl(user.getChatId(), link.getUrl()).get();
            user.getLinks().add(link);
            adedlink.getUsers().add(user);
        }
    }

    public boolean wasLinkTracked(User user, Link link) {
        return user.getLinks().stream()
            .anyMatch(userLink -> Objects.equals(userLink.getUrl(), link.getUrl()));
    }
}

