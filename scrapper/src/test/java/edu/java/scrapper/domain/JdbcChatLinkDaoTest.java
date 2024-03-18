package edu.java.scrapper.domain;

import edu.java.domain.chat_link.ChatLinkMapper;
import edu.java.domain.chat_link.JdbcChatLinkDao;
import edu.java.domain.dto.ChatLink;
import edu.java.domain.dto.Link;
import edu.java.domain.link.JdbcLinkDao;
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
public class JdbcChatLinkDaoTest extends IntegrationTest {

    private static final ChatLinkMapper MAPPER = new ChatLinkMapper();

    @Autowired
    JdbcChatLinkDao underTest;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        jdbcTemplate.update("INSERT INTO chat VALUES (1)");
        jdbcTemplate.update(
            "INSERT INTO link VALUES (?, ?, ?, ?)",
            1,
            "test.com",
            OffsetDateTime.now(),
            OffsetDateTime.now()
        );
        underTest.add(1L, 1L);
        final Optional<ChatLink> findChatLink =
            jdbcTemplate.query("SELECT * FROM chat_link", MAPPER).stream().findFirst();
        Assertions.assertTrue(findChatLink.isPresent());
        Assertions.assertEquals(1L, findChatLink.get().getChatId());
        Assertions.assertEquals(1, findChatLink.get().getLinkId());
    }

    @Test
    @Transactional
    @Rollback
    void findTest() {
        jdbcTemplate.update("INSERT INTO chat VALUES (1)");
        jdbcTemplate.update(
            "INSERT INTO link VALUES (?, ?, ?, ?)",
            1,
            "test.com",
            OffsetDateTime.now(),
            OffsetDateTime.now()
        );
        jdbcTemplate.update("INSERT INTO chat_link VALUES (?, ?)", 1L, 1L);
        final Optional<ChatLink> actualResult = underTest.find(1L, 1L);
        Assertions.assertTrue(actualResult.isPresent());
        Assertions.assertEquals(1L, actualResult.get().getChatId());
        Assertions.assertEquals(1, actualResult.get().getLinkId());
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        jdbcTemplate.update("INSERT INTO chat VALUES (1)");
        jdbcTemplate.update(
            "INSERT INTO link VALUES (?, ?, ?, ?)",
            1,
            "test.com",
            OffsetDateTime.now(),
            OffsetDateTime.now()
        );
        jdbcTemplate.update("INSERT INTO chat_link VALUES (?, ?)", 1L, 1);
        final List<ChatLink> actualResult = underTest.findAll();
        Assertions.assertEquals(1, actualResult.size());
        Assertions.assertEquals(1, actualResult.get(0).getChatId());
        Assertions.assertEquals(1, actualResult.get(0).getLinkId());
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
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
        jdbcTemplate.update("INSERT INTO chat_link VALUES (?, ?)", 1L, 1);
        underTest.remove(1L, link);
        Optional<ChatLink> actualResult = jdbcTemplate.query("SELECT * FROM chat_link", MAPPER).stream().findFirst();
        Assertions.assertTrue(actualResult.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void findChatsByLinkTest() {
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
        jdbcTemplate.update("INSERT INTO chat_link VALUES (?, ?)", 1L, 1);
        final List<Long> actualResult = underTest.findChatsByLink(link);
        Assertions.assertEquals(1L, actualResult.size());
        Assertions.assertEquals(1L, actualResult.get(0));
    }

    @Test
    @Transactional
    @Rollback
    void isLinkPresentTest() {
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
        jdbcTemplate.update("INSERT INTO chat_link VALUES (?, ?)", 1L, 1);
        Assertions.assertTrue(underTest.isLinkPresent(link));
    }
}
