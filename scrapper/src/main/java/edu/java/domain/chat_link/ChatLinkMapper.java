package edu.java.domain.chat_link;

import edu.java.domain.dto.jdbc.ChatLink;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ChatLinkMapper implements RowMapper<ChatLink> {
    @Override
    public ChatLink mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new ChatLink(
            resultSet.getLong("chat_id"),
            resultSet.getLong("link_id")
        );
    }
}
