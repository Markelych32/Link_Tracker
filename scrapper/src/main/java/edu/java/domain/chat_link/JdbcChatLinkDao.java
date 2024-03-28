package edu.java.domain.chat_link;

import edu.java.domain.dto.ChatLink;
import edu.java.domain.dto.Link;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatLinkDao implements ChatLinkDao {

    private static final String ADD_SQL = "INSERT INTO chat_link VALUES (:chatId, :linkId)";
    private static final String REMOVE_SQL = "DELETE FROM chat_link WHERE chat_id = :chatId AND link_id = :linkId";
    private static final String FIND_ALL_SQL = "SELECT chat_id, link_id FROM chat_link";
    private static final String FIND_SQL =
        "SELECT chat_id, link_id FROM chat_link WHERE chat_id = :chatId AND link_id = :linkId";
    private static final String FIND_BY_LINK_SQL = "SELECT chat_id FROM chat_link WHERE link_id = :linkId";

    private static final String CHAT_ID_PARAMETER = "chatId";
    private static final String LINK_ID_PARAMETER = "linkId";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ChatLinkMapper chatLinkMapper;

    @Override
    public boolean add(Long chatId, Long linkId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue(CHAT_ID_PARAMETER, chatId)
            .addValue(LINK_ID_PARAMETER, linkId);
        return namedParameterJdbcTemplate.update(ADD_SQL, namedParameters) > 0;
    }

    @Override
    public boolean remove(Long chatId, Link link) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue(CHAT_ID_PARAMETER, chatId)
            .addValue(LINK_ID_PARAMETER, link.getId());
        return namedParameterJdbcTemplate.update(REMOVE_SQL, namedParameters) > 0;
    }

    @Override
    public List<ChatLink> findAll() {
        return namedParameterJdbcTemplate.query(FIND_ALL_SQL, chatLinkMapper);
    }

    @Override
    public Optional<ChatLink> find(Long chatId, Long linkId) {
        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue(CHAT_ID_PARAMETER, chatId)
                .addValue(LINK_ID_PARAMETER, linkId);
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(
                FIND_SQL,
                namedParameters,
                chatLinkMapper
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Long> findChatsByLink(Link link) {
        SqlParameterSource namedParameters = new MapSqlParameterSource(LINK_ID_PARAMETER, link.getId());
        return namedParameterJdbcTemplate.queryForList(FIND_BY_LINK_SQL, namedParameters, Long.class);
    }

    @Override
    public boolean isLinkPresent(Link link) {
        return !findChatsByLink(link).isEmpty();
    }
}
