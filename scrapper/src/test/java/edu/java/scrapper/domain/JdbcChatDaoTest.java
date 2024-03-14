package edu.java.scrapper.domain;

import edu.java.domain.chat.ChatMapper;
import edu.java.domain.chat.JdbcChatDao;
import edu.java.domain.dto.Chat;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JdbcChatDaoTest extends IntegrationTest {

    private final JdbcChatDao underTest;
    private final JdbcTemplate jdbcTemplate;
    private final ChatMapper chatMapper = new ChatMapper();

    @Autowired
    public JdbcChatDaoTest(JdbcChatDao underTest, JdbcTemplate jdbcTemplate) {
        this.underTest = underTest;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Test
    @Transactional
    @Rollback
    void addTest() {
        jdbcTemplate.update("DELETE FROM chat");
        underTest.add(1L);
        Chat chat = jdbcTemplate.queryForObject("SELECT * FROM chat WHERE tg_chat_id = 1", chatMapper);
        assert chat != null;
        Assertions.assertEquals(1L, chat.getTgChatId());
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        jdbcTemplate.update("DELETE FROM chat");
        underTest.add(1L);
        underTest.remove(1L);
        Integer rowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM chat WHERE tg_chat_id = 1", Integer.class);
        Assertions.assertEquals(rowCount, 0);
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        jdbcTemplate.update("DELETE FROM chat");
        underTest.add(1L);
        underTest.add(2L);
        Assertions.assertEquals(underTest.findAll().size(), 2);
    }
}
