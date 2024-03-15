package edu.java.domain.link;

import edu.java.domain.dto.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcLinkDao implements LinkDao {

    private static final String ADD_SQL = "INSERT INTO link (url, last_update, last_check) VALUES (?, ?, ?)";
    private static final String REMOVE_SQL = "DELETE FROM link WHERE url = ?";
    private static final String FIND_ALL_SQL = "SELECT id, url, last_update, last_check FROM link";
    private static final String FIND_SQL = "SELECT id, url, last_update, last_check FROM link WHERE url = ?";
    private static final String UPDATE_SQL = "UPDATE link SET last_update = ?, last_check = ? WHERE url = ?";

    private final JdbcTemplate jdbcTemplate;

    private final LinkMapper linkRowMapper = new LinkMapper();

    @Override
    public boolean add(Link link) {
        return jdbcTemplate.update(ADD_SQL, link.getUrl(), link.getLastUpdate(), link.getLastCheck()) >
               0;
    }

    @Override
    public boolean remove(String url) {
        return jdbcTemplate.update(REMOVE_SQL, url) > 0;
    }

    @Override
    public List<Link> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, linkRowMapper);
    }

    @Override
    public Optional<Link> find(String url) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_SQL, linkRowMapper, url));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public boolean update(Link link) {
        return
            jdbcTemplate.update(UPDATE_SQL, linkRowMapper, link.getLastUpdate(), link.getLastCheck(), link.getUrl()) >
            0;
    }

}
