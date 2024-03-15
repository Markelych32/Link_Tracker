package edu.java.domain.chat_link;

import edu.java.domain.chat.ChatMapper;
import edu.java.domain.dto.ChatLink;
import edu.java.domain.dto.Link;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatLinkDao implements ChatLinkDao {

    private static final String ADD_SQL = "INSERT INTO chat_link VALUES (?, ?)";
    private static final String REMOVE_SQL = "DELETE FROM chat_link WHERE chat_id = ? AND link_id = ?";
    private static final String FIND_ALL_SQL = "SELECT chat_id, link_id FROM chat_link";
    private static final String FIND_SQL = "SELECT chat_id, link_id FROM chat_link WHERE chat_id = ? AND link_id = ?";
    private static final String FIND_BY_LINK_SQL = "SELECT chat_id FROM chat_link WHERE link_id = ?";

    private final JdbcTemplate jdbcTemplate;

    private final ChatLinkMapper chatLinkMapper = new ChatLinkMapper();
    private final ChatMapper chatMapper = new ChatMapper();

    @Override
    public boolean add(Long chatId, Long linkId) {
        return jdbcTemplate.update(ADD_SQL, chatId, linkId) > 0;
    }

    @Override
    public boolean remove(Long chatId, Link link) {
        return jdbcTemplate.update(REMOVE_SQL, chatId, link.getId()) > 0;
    }

    @Override
    public List<ChatLink> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, chatLinkMapper);
    }

    @Override
    public Optional<ChatLink> find(Long chatId, Long linkId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_SQL, chatLinkMapper, chatId, linkId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Long> findChatsByLink(Link link) {
        return jdbcTemplate.queryForList(FIND_BY_LINK_SQL, Long.class, link.getId());
    }

    @Override
    public boolean isLinkPresent(Link link) {
        return !findChatsByLink(link).isEmpty();
    }
}
