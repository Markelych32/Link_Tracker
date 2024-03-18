package edu.java.scrapper.domain.chat;

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

    private static final ChatMapper CHAT_MAPPER = new ChatMapper();

    @Autowired
    JdbcChatDao underTest;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        underTest.add(1L);
        Optional<Chat> actualResult = jdbcTemplate.query("SELECT id FROM chat", CHAT_MAPPER).stream().findFirst();
        Assertions.assertTrue(actualResult.isPresent());
        Assertions.assertEquals(1L, actualResult.get().getId());
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        jdbcTemplate.update("INSERT INTO chat VALUES (1)");
        boolean actualResult = underTest.remove(1L);
        Assertions.assertTrue(actualResult);
        Optional<Chat> actualChat = jdbcTemplate.query("SELECT id FROM chat", CHAT_MAPPER).stream().findFirst();
        Assertions.assertFalse(actualChat.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        jdbcTemplate.update("INSERT INTO chat VALUES (1), (2)");
        List<Long> actualResult = underTest.findAll().stream().map(Chat::getId).toList();
        List<Long> expectedResult = List.of(1L, 2L);
        Assertions.assertEquals(actualResult, expectedResult);
    }

    @Test
    @Transactional
    @Rollback
    void findExistChatTestShouldReturnChat() {
        jdbcTemplate.update("INSERT INTO chat VALUES (1)");
        Optional<Chat> actualResult = underTest.find(1L);
        Assertions.assertTrue(actualResult.isPresent());
        Assertions.assertEquals(1L, actualResult.get().getId());
    }
}
