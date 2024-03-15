package edu.java.scrapper.domain;

import edu.java.domain.chat.ChatMapper;
import edu.java.domain.chat.JdbcChatDao;
import edu.java.domain.dto.Chat;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

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
        underTest.add(1L);
        Chat chat = jdbcTemplate.queryForObject("SELECT * FROM chat WHERE id = 1", chatMapper);
        assert chat != null;
        Assertions.assertEquals(1L, chat.getId());
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        underTest.add(1L);
        boolean result = underTest.remove(1L);
        Integer rowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM chat WHERE id = 1", Integer.class);
        Assertions.assertEquals(rowCount, 0);
        Assertions.assertTrue(result);
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        underTest.add(1L);
        underTest.add(2L);
        List<Long> chats = underTest.findAll().stream().map(Chat::getId).toList();
        Assertions.assertEquals(List.of(1L, 2L), chats);
    }

    @Test
    @Transactional
    @Rollback
    void findExistChatTestShouldReturnChat() {
        underTest.add(1L);
        Optional<Chat> chat = underTest.find(1L);
        Assertions.assertEquals(1L, chat.get().getId());
    }
}
