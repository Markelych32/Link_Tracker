package edu.java.domain.chat;

import edu.java.domain.dto.Chat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcChatDao implements ChatDao {

    private static final String ADD_SQL = "INSERT INTO chat(tg_chat_id) VALUES (?)";
    private static final String REMOVE_SQL = "DELETE FROM chat WHERE tg_chat_id = ?";
    private static final String FIND_ALL_SQL = "SELECT id, tg_chat_id FROM chat";

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Chat> chatRowMapper = (resultSet, rowNum) -> new Chat(
        resultSet.getLong("id"),
        resultSet.getLong("tg_chat_id")
    );

    @Override
    public boolean add(Chat chat) {
        return jdbcTemplate.update(ADD_SQL, chat.getTgChatId()) > 0;
    }

    @Override
    public boolean remove(Chat chat) {
        return jdbcTemplate.update(REMOVE_SQL, chat.getTgChatId()) > 0;
    }

    @Override
    public List<Chat> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, chatRowMapper);
    }
}
