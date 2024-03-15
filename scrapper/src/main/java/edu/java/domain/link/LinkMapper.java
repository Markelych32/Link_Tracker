package edu.java.domain.link;

import edu.java.domain.dto.Link;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class LinkMapper implements RowMapper<Link> {
    @Override
    public Link mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Link(
            resultSet.getLong("id"),
            resultSet.getString("url"),
            resultSet.getObject(3, OffsetDateTime.class),
            resultSet.getObject(4, OffsetDateTime.class)
        );
    }
}
