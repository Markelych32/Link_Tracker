package edu.java.scrapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CheckInitTablesTest extends IntegrationTest {
    @Test
    void checkDatabaseAfterMigrations() throws SQLException {
        Connection connection = DriverManager.getConnection(
            POSTGRES.getJdbcUrl(),
            POSTGRES.getUsername(),
            POSTGRES.getPassword()
        );
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[] {"TABLE"});
        List<String> tables = new ArrayList<>();
        while (resultSet.next()) {
            tables.add(resultSet.getString("TABLE_NAME"));
        }
        Assertions.assertEquals(
            List.of("chat", "chat_link", "databasechangelog", "databasechangeloglock", "link"),
            tables
        );
    }
}
