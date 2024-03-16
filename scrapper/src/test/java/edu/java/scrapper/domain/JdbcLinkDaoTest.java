package edu.java.scrapper.domain;

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
import java.util.stream.Collectors;

@SpringBootTest
public class JdbcLinkDaoTest extends IntegrationTest {

    private final JdbcLinkDao underTest;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcLinkDaoTest(JdbcLinkDao jdbcLinkDao, JdbcTemplate jdbcTemplate) {
        this.underTest = jdbcLinkDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Test
    @Transactional
    @Rollback
    void addTest() {
        jdbcTemplate.update("DELETE FROM chat_link WHERE chat_id = 1");
        jdbcTemplate.update("DELETE FROM chat WHERE id = 1");
        jdbcTemplate.update("DELETE FROM link WHERE id = 1");
        Link link = TestData.testLinkDtoFirst();
        underTest.add(link);
        Optional<Link> findLink = underTest.find(link.getUrl());
        Assertions.assertTrue(findLink.isPresent());
        Assertions.assertEquals(link.getUrl(), findLink.get().getUrl());
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        jdbcTemplate.update("DELETE FROM chat_link WHERE chat_id = 1");
        jdbcTemplate.update("DELETE FROM chat WHERE id = 1");
        jdbcTemplate.update("DELETE FROM link WHERE id = 1");
        Link link = TestData.testLinkDtoFirst();
        underTest.remove(link.getUrl());
        Optional<Link> linkFromDB = underTest.find(link.getUrl());
        Assertions.assertTrue(linkFromDB.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        jdbcTemplate.update("DELETE FROM chat_link");
        jdbcTemplate.update("DELETE FROM link");
        Link link1 = TestData.testLinkDtoFirst();
        Link link2 = TestData.testLinkDtoSecond();
        underTest.add(link1);
        underTest.add(link2);
        List<String> urls = underTest.findAll().stream().map(Link::getUrl).toList();
        Assertions.assertEquals(List.of(link1.getUrl(), link2.getUrl()), urls);
    }
}
