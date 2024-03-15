package edu.java.domain.chat;

import edu.java.domain.dto.Chat;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatMapper implements RowMapper<Chat> {
    @Override
    public Chat mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Chat(
            resultSet.getLong("id")
        );
    }
}
