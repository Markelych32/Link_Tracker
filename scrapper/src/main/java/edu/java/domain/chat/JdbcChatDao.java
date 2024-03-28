package edu.java.domain.chat;

import edu.java.domain.dto.Chat;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcChatDao implements ChatDao {

    private static final String ADD_SQL = "INSERT INTO chat(id) VALUES (?)";
    private static final String REMOVE_SQL = "DELETE FROM chat WHERE id = ?";
    private static final String FIND_ALL_SQL = "SELECT id FROM chat";
    private static final String FIND_SQL = "SELECT id FROM chat WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final ChatMapper chatRowMapper;

    @Override
    public boolean add(Long tgChatId) {
        return jdbcTemplate.update(ADD_SQL, tgChatId) > 0;
    }

    @Override
    public boolean remove(Long tgChatId) {
        return jdbcTemplate.update(REMOVE_SQL, tgChatId) > 0;
    }

    @Override
    public List<Chat> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, chatRowMapper);
    }

    @Override
    public Optional<Chat> find(Long tgChatId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_SQL, chatRowMapper, tgChatId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }
}
