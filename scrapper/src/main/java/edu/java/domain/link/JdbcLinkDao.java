package edu.java.domain.link;

import edu.java.domain.dto.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcLinkDao implements LinkDao {

    private static final String ADD_TO_LINK_SQL = "INSERT INTO link (url, last_update) VALUES (?, ?) RETURNING ID";
    private static final String ADD_TO_CHAT_LINK_SQL = "INSERT INTO chat_link VALUES (?, ?)";
    private static final String REMOVE_SQL = """
        DELETE FROM chat_link
        USING chat, link
        WHERE chat.tg_chat_id = ? AND link.url = ? AND link.id = chat_link.link_id
        """;
    private static final String FIND_ALL_SQL = "SELECT id, url, last_update FROM link";

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Link> linkRowMapper = (resultSet, rowNum) -> new Link(
        resultSet.getLong("id"),
        resultSet.getString("url"),
        resultSet.getTimestamp("last_update")
    );

    @Override
    public boolean add(Long chatId, Link link) {
        Long linkId = jdbcTemplate.queryForObject(ADD_TO_LINK_SQL, Long.class, link.getUrl(), link.getLastUpdate());
        return jdbcTemplate.update(ADD_TO_CHAT_LINK_SQL, chatId, linkId) > 0;
    }

    @Override
    public boolean remove(Long tgChatId, String url) {
        return jdbcTemplate.update(REMOVE_SQL, tgChatId, url) > 0;
    }

    @Override
    public List<Link> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, linkRowMapper);
    }
}
