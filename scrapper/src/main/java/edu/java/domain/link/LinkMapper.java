package edu.java.domain.link;

import edu.java.domain.dto.Link;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class LinkMapper implements RowMapper<Link> {

    private static final int RESULT_SET_NUM_OF_LAST_UPDATE = 3;
    private static final int RESULT_SET_NUM_OF_LAST_CHECK = 4;

    @Override
    public Link mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Link(
            resultSet.getLong("id"),
            resultSet.getString("url"),
            resultSet.getObject(RESULT_SET_NUM_OF_LAST_UPDATE, OffsetDateTime.class),
            resultSet.getObject(RESULT_SET_NUM_OF_LAST_CHECK, OffsetDateTime.class)
        );
    }
}
