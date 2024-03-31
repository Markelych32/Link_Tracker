package edu.java.scrapper.domain.link;

import edu.java.domain.dto.jdbc.Link;
import edu.java.domain.link.JdbcLinkDao;
import edu.java.domain.link.LinkMapper;
import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class JdbcLinkDaoTest extends IntegrationTest {

    private static final LinkMapper LINK_MAPPER = new LinkMapper();

    @Autowired
    JdbcLinkDao underTest;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        Link link = TestData.testLinkDtoFirst();
        underTest.add(link);
        Optional<Link> actualLink = jdbcTemplate.query("SELECT * from link", LINK_MAPPER).stream().findFirst();
        Assertions.assertTrue(actualLink.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        Link link = TestData.testLinkDtoFirst();
        jdbcTemplate.update(
            "INSERT INTO link(url, last_update, last_check) VALUES (?, ?, ?)",
            link.getUrl(),
            link.getLastUpdate(),
            link.getLastCheck()
        );
        underTest.remove(link.getUrl());
        Optional<Link> actualResult =
            jdbcTemplate.query("SELECT url, last_update, last_check FROM link", LINK_MAPPER).stream().findFirst();
        Assertions.assertTrue(actualResult.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        Link link1 = TestData.testLinkDtoFirst();
        Link link2 = TestData.testLinkDtoSecond();
        jdbcTemplate.update(
            "INSERT INTO link(url, last_update, last_check) VALUES (?, ?, ?), (?, ?, ?)",
            link1.getUrl(),
            link1.getLastUpdate(),
            link1.getLastCheck(),
            link2.getUrl(),
            link2.getLastUpdate(),
            link2.getLastCheck()
        );
        List<String> actualResult = underTest.findAll().stream().map(Link::getUrl).toList();
        Assertions.assertEquals(List.of(link1.getUrl(), link2.getUrl()), actualResult);
    }

    @Test
    @Transactional
    @Rollback
    void findTest() {
        Link link = TestData.testLinkDtoFirst();
        jdbcTemplate.update(
            "INSERT INTO link(url, last_update, last_check) VALUES (?, ?, ?)",
            link.getUrl(),
            link.getLastUpdate(),
            link.getLastCheck()
        );
        Optional<Link> actualResult = underTest.find(link.getUrl());
        Assertions.assertTrue(actualResult.isPresent());
        Assertions.assertEquals(link.getUrl(), actualResult.get().getUrl());
    }

    @Test
    @Transactional
    @Rollback
    void findAllByChat() {
        Link link = new Link(
            1L,
            "test.com",
            OffsetDateTime.now(),
            OffsetDateTime.now()
        );
        jdbcTemplate.update("INSERT INTO chat VALUES (1)");
        jdbcTemplate.update(
            "INSERT INTO link VALUES (?, ?, ?, ?)",
            1,
            "test.com",
            OffsetDateTime.now(),
            OffsetDateTime.now()
        );
        jdbcTemplate.update("INSERT INTO chat_link VALUES (?, ?)", 1L, 1L);
        List<Link> actualResult = underTest.findAllByChat(1L);
        Assertions.assertEquals(1, actualResult.size());
    }

    @Test
    @Transactional
    @Rollback
    void updateTest() {
        Link link = TestData.testLinkDtoFirst();
        Link newLink = new Link();
        jdbcTemplate.update(
            "INSERT INTO link (url, last_update, last_check) VALUES (?, ?, ?)",
            link.getUrl(),
            link.getLastUpdate(),
            link.getLastCheck()
        );
        newLink.setUrl(link.getUrl());
        newLink.setLastUpdate(OffsetDateTime.MIN);
        newLink.setLastCheck(OffsetDateTime.MIN);
        underTest.update(newLink);
        Optional<Link> actualResult =
            jdbcTemplate.query("SELECT * FROM link WHERE url = ?", LINK_MAPPER, link.getUrl()).stream().findFirst();
        Assertions.assertTrue(actualResult.isPresent());
        Assertions.assertEquals(OffsetDateTime.MIN, actualResult.get().getLastUpdate());
    }
}
