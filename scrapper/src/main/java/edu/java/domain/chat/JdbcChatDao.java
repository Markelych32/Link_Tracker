package edu.java.domain.chat;

import edu.java.domain.dto.jdbc.Chat;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcChatDao implements ChatDao {

    private static final String ADD_SQL = "INSERT INTO chat(id) VALUES (:id)";
    private static final String REMOVE_SQL = "DELETE FROM chat WHERE id = :id";
    private static final String FIND_ALL_SQL = "SELECT id FROM chat";
    private static final String FIND_SQL = "SELECT id FROM chat WHERE id = :id";

    private final ChatMapper chatRowMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public boolean add(Long tgChatId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", tgChatId);
        return namedParameterJdbcTemplate.update(ADD_SQL, namedParameters) > 0;
    }

    @Override
    public boolean remove(Long tgChatId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", tgChatId);
        return namedParameterJdbcTemplate.update(REMOVE_SQL, namedParameters) > 0;
    }

    @Override
    public List<Chat> findAll() {
        return namedParameterJdbcTemplate.query(FIND_ALL_SQL, chatRowMapper);
    }

    @Override
    public Optional<Chat> find(Long tgChatId) {
        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource("id", tgChatId);
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(
                FIND_SQL,
                namedParameters,
                chatRowMapper
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }
}
