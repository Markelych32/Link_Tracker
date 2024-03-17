package edu.java.scrapper;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

@Testcontainers
@SpringBootTest
@Transactional
public abstract class IntegrationTest {

    @ServiceConnection
    static PostgreSQLContainer<?> POSTGRES;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("scrapper")
            .withUsername("postgres")
            .withPassword("postgres");
        POSTGRES.start();

        try {
            runMigrations(POSTGRES);
        } catch (SQLException | LiquibaseException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void restartIdentity() {
        jdbcTemplate.update("TRUNCATE chat_link RESTART IDENTITY");
    }

    private static void runMigrations(JdbcDatabaseContainer<?> c)
        throws SQLException, LiquibaseException, FileNotFoundException {
        Connection connection = POSTGRES.createConnection("");
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
            new JdbcConnection(connection));
        Path changelogPath = new File(".").toPath().toAbsolutePath().getParent().getParent().resolve("migrations");
        Liquibase liquibase = new Liquibase(
            "master.xml",
            new DirectoryResourceAccessor(changelogPath),
            database
        );
        liquibase.update(new Contexts(), new LabelExpression());
    }
}
