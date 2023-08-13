package carsharing.util;

import java.sql.SQLException;
import java.sql.Statement;

public class SqlUtils {

    private SqlUtils() {
        // nth
    }

    public static void createTableCompany(Statement statement) throws SQLException {
        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS COMPANY " +
                        "(" +
                        "ID INTEGER NOT NULL auto_increment PRIMARY KEY, " +
                        "NAME VARCHAR(250) UNIQUE NOT NULL" +
                        ")"
        );
    }

    public static void dropTableCompanyIfExists(Statement statement) throws SQLException {
        statement.executeUpdate("DROP TABLE IF EXISTS COMPANY");
    }

    public static void createTableCar(Statement statement) throws SQLException {
        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS CAR " +
                        "(" +
                        "ID INTEGER NOT NULL auto_increment PRIMARY KEY, " +
                        "NAME VARCHAR(250) UNIQUE NOT NULL, " +
                        "COMPANY_ID INTEGER NOT NULL, " +
                        "CONSTRAINT fk_company_car FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY (ID)" +
                        ")"
        );
    }

    public static void dropTableCarIfExists(Statement statement) throws SQLException {
        statement.executeUpdate("DROP TABLE IF EXISTS CAR");
    }
}