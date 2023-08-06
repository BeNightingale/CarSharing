package carsharing.util;

import java.sql.SQLException;
import java.sql.Statement;

public class SqlUtils {

    public static void createTableCompany(Statement statement) throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS COMPANY (" +
                "ID INTEGER NOT NULL PRIMARY KEY, " +
                "NAME VARCHAR(250) NOT NULL)");
    }

    public static void dropTableCompanyIfExists(Statement statement) throws SQLException {
        statement.executeUpdate("DROP TABLE IF EXISTS COMPANY");
    }
}