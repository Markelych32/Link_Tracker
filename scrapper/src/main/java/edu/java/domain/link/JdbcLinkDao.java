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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkDao implements LinkDao {

    private static final String ADD_SQL =
        "INSERT INTO link (url, last_update, last_check) VALUES (?, ?, ?)";
    private static final String REMOVE_SQL = "DELETE FROM link WHERE url = :url";
    private static final String FIND_ALL_SQL = "SELECT id, url, last_update, last_check FROM link";
    private static final String FIND_SQL = "SELECT id, url, last_update, last_check FROM link WHERE url = :url";
    private static final String FIND_ALL_BY_CHAT_SQL = """
        SELECT id, url, last_update, last_check
        FROM link
        JOIN chat_link ON link.id = chat_link.link_id
        WHERE chat_id = :chatId
        """;
    private static final String UPDATE_SQL =
        "UPDATE link SET last_update = :lastUpdate, last_check = :lastCheck WHERE url = :url";

    private static final String URL_PARAMETER = "url";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final LinkMapper linkRowMapper;

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
                    null
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
        SqlParameterSource namedParameters = new MapSqlParameterSource(URL_PARAMETER, url);
        return namedParameterJdbcTemplate.update(REMOVE_SQL, namedParameters) > 0;
    }

    @Override
    public List<Link> findAll() {
        return namedParameterJdbcTemplate.query(FIND_ALL_SQL, linkRowMapper);
    }

    @Override
    public Optional<Link> find(String url) {
        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource(URL_PARAMETER, url);
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(
                FIND_SQL,
                namedParameters,
                linkRowMapper
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public List<Link> findAllByChat(Long tgcChatId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("chatId", tgcChatId);
        return namedParameterJdbcTemplate.query(FIND_ALL_BY_CHAT_SQL, namedParameters, linkRowMapper);
    }

    @Override
    public void update(Link link) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("lastUpdate", link.getLastUpdate())
            .addValue("lastCheck", link.getLastCheck())
            .addValue(URL_PARAMETER, link.getUrl());
        namedParameterJdbcTemplate.update(UPDATE_SQL, namedParameters);
    }

}
