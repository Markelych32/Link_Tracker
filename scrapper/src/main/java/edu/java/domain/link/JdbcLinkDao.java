package edu.java.domain.link;

import edu.java.domain.dto.Link;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkDao implements LinkDao {

    private static final String ADD_SQL =
        "INSERT INTO link (url, last_update, last_check) VALUES (?, ?, ?)";
    private static final String REMOVE_SQL = "DELETE FROM link WHERE url = ?";
    private static final String FIND_ALL_SQL = "SELECT id, url, last_update, last_check FROM link";
    private static final String FIND_SQL = "SELECT id, url, last_update, last_check FROM link WHERE url = ?";
    private static final String FIND_ALL_BY_CHAT_SQL = """
        SELECT id, url, last_update, last_check
        FROM link
        JOIN chat_link ON link.id = chat_link.link_id
        WHERE chat_id = ?
        """;
    private static final String UPDATE_SQL = "UPDATE link SET last_update = ?, last_check = ? WHERE url = ?";

    private final JdbcTemplate jdbcTemplate;

    private final LinkMapper linkRowMapper = new LinkMapper();

    private static final int COLUMN_NUM_OF_LAST_UPDATE = 2;
    private static final int COLUMN_NUM_OF_LAST_CHECK = 3;

    @Override
    public Long add(Link link) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public @NotNull PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement preparedStatement = con.prepareStatement(ADD_SQL, new String[] {"id"});
                preparedStatement.setString(1, link.getUrl());
                preparedStatement.setTimestamp(
                    COLUMN_NUM_OF_LAST_UPDATE,
                    Timestamp.valueOf(link.getLastUpdate().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime())
                );
                preparedStatement.setTimestamp(
                    COLUMN_NUM_OF_LAST_CHECK,
                    Timestamp.valueOf(link.getLastCheck().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime())
                );
                return preparedStatement;
            }
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public boolean remove(String url) {
        return jdbcTemplate.update(REMOVE_SQL, url) > 0;
    }

    @Override
    public List<Link> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, linkRowMapper);
    }

    @Override
    public Optional<Link> find(String url) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_SQL, linkRowMapper, url));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public List<Link> findAllByChat(Long tgcChatId) {
        return jdbcTemplate.query(FIND_ALL_BY_CHAT_SQL, linkRowMapper, tgcChatId);
    }

    @Override
    public void update(Link link) {
        jdbcTemplate.update(UPDATE_SQL, link.getLastUpdate(), link.getLastCheck(), link.getUrl());
    }

}
