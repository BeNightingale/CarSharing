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
                        "IS_RENTED Boolean, " +
                        "CONSTRAINT fk_company_car FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY (ID)" +
                        ")"
        );
    }
    public static void alterTableCarAddColumn(Statement statement) throws SQLException {
        statement.executeUpdate(
                "ALTER TABLE CAR ADD COLUMN IS_RENTED BOOLEAN"
        );
    }

    public static void dropTableCarIfExists(Statement statement) throws SQLException {
        statement.executeUpdate("DROP TABLE IF EXISTS CAR");
    }

    public static void createTableCustomer(Statement statement) throws SQLException {
        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                        "(" +
                        "ID INTEGER NOT NULL auto_increment PRIMARY KEY, " +
                        "NAME VARCHAR(250) UNIQUE NOT NULL, " +
                        "RENTED_CAR_ID INTEGER, " +
                        "CONSTRAINT fk_car_customer FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR (ID)" +
                        ")"
        );
    }

    public static void alterTableCustomer(Statement statement) throws SQLException {
        statement.executeUpdate(
                "ALTER TABLE CUSTOMER ALTER COLUMN ID RESTART WITH 1"
        );
    }
}