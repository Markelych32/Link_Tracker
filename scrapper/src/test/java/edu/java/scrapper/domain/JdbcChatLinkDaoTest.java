package edu.java.scrapper.domain;

import edu.java.domain.chat_link.ChatLinkMapper;
import edu.java.domain.chat_link.JdbcChatLinkDao;
import edu.java.domain.dto.ChatLink;
import edu.java.domain.dto.Link;
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

    private final JdbcChatLinkDao underTest;
    private final JdbcTemplate jdbcTemplate;

    private final ChatLinkMapper chatLinkMapper = new ChatLinkMapper();

    @Autowired
    public JdbcChatLinkDaoTest(JdbcChatLinkDao jdbcChatLinkDao, JdbcTemplate jdbcTemplate) {
        underTest = jdbcChatLinkDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Test
    @Transactional
    @Rollback
    void addTest() {
        jdbcTemplate.update("INSERT INTO chat VALUES (1)");
        jdbcTemplate.update("""
            INSERT INTO link
            VALUES (?, ?, ?, ?)
            """, 1L, "test-url", OffsetDateTime.now(), OffsetDateTime.now());
        final ChatLink chatLink = new ChatLink(1L, 1L);
        underTest.add(1L, 1L);
        final Optional<ChatLink> findChatLink = underTest.find(1L, 1L);
        Assertions.assertTrue(findChatLink.isPresent());
        Assertions.assertEquals(1L, findChatLink.get().getChatId());
        Assertions.assertEquals(1L, findChatLink.get().getLinkId());
    }

    @Test
    @Transactional
    @Rollback
    void findTest() {
        jdbcTemplate.update("INSERT INTO chat VALUES (1)");
        jdbcTemplate.update("""
            INSERT INTO link
            VALUES (?, ?, ?, ?)
            """, 1L, "test-url", OffsetDateTime.now(), OffsetDateTime.now());
        jdbcTemplate.update("""
            INSERT INTO chat_link
            VALUES (1, 1)
            """);
        final Optional<ChatLink> actualResult = underTest.find(1L, 1L);
        Assertions.assertTrue(actualResult.isPresent());
        Assertions.assertEquals(1L, actualResult.get().getChatId());
        Assertions.assertEquals(1L, actualResult.get().getLinkId());
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        jdbcTemplate.update("INSERT INTO chat VALUES (1)");
        jdbcTemplate.update("""
            INSERT INTO link
            VALUES (?, ?, ?, ?)
            """, 1L, "test-url", OffsetDateTime.now(), OffsetDateTime.now());
        jdbcTemplate.update("""
            INSERT INTO chat_link
            VALUES (1, 1)
            """);
        final List<ChatLink> actualResult = underTest.findAll();
        Assertions.assertEquals(1, actualResult.size());
        Assertions.assertEquals(1, actualResult.get(0).getChatId());
        Assertions.assertEquals(1, actualResult.get(0).getLinkId());
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        jdbcTemplate.update("INSERT INTO chat VALUES (1)");
        jdbcTemplate.update("""
            INSERT INTO link
            VALUES (?, ?, ?, ?)
            """, 1L, "test-url", OffsetDateTime.now(), OffsetDateTime.now());
        jdbcTemplate.update("""
            INSERT INTO chat_link
            VALUES (1, 1)
            """);
        Link link = new Link();
        link.setId(1L);
        underTest.remove(1L, link);
        final List<ChatLink> findChatLink =
            jdbcTemplate.query("SELECT chat_id, link_id FROM chat_link", chatLinkMapper);
        Assertions.assertEquals(0, findChatLink.size());
    }

    @Test
    @Transactional
    @Rollback
    void findChatsByLinkTest() {
        jdbcTemplate.update("INSERT INTO chat VALUES (1)");
        jdbcTemplate.update("""
            INSERT INTO link
            VALUES (?, ?, ?, ?)
            """, 1L, "test-url", OffsetDateTime.now(), OffsetDateTime.now());
        jdbcTemplate.update("""
            INSERT INTO chat_link
            VALUES (1, 1)
            """);
        Link link = TestData.testLinkDtoFirst();
        link.setId(1L);
        final List<Long> findChats = underTest.findChatsByLink(link);
        Assertions.assertEquals(1L, findChats.size());
        Assertions.assertEquals(1L, findChats.get(0));
    }

    @Test
    @Transactional
    @Rollback
    void isLinkPresentTest() {
        jdbcTemplate.update("INSERT INTO chat VALUES (1)");
        jdbcTemplate.update("""
            INSERT INTO link
            VALUES (?, ?, ?, ?)
            """, 1L, "test-url", OffsetDateTime.now(), OffsetDateTime.now());
        jdbcTemplate.update("""
            INSERT INTO chat_link
            VALUES (1, 1)
            """);
        Link link = TestData.testLinkDtoFirst();
        link.setId(1L);
        Assertions.assertTrue(underTest.isLinkPresent(link));
    }
}
