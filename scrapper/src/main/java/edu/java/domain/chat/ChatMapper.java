package edu.java.domain.chat;

import edu.java.domain.dto.jdbc.Chat;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper implements RowMapper<Chat> {
    @Override
    public Chat mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Chat(
            resultSet.getLong("id")
        );
    }
}
