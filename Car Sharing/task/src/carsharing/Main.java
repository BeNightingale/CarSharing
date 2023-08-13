package carsharing;

import carsharing.util.SqlUtils;
import carsharing.util.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class Main {

    public static void main(String[] args) {

        final String databaseFileName = Utils.getFirstArgsParameter(args, "-databaseFileName", "carsharing");
        System.out.println(databaseFileName);

        final String URL = "jdbc:h2:./src/carsharing/db/" + databaseFileName;

        try (Connection connection = DriverManager.getConnection(URL);
             final Statement statement = connection.createStatement()
        ) {
            connection.setAutoCommit(true);
            initDatabaseTables(statement);
            final CompanyService service = new CompanyService(
                    new CompanyRepository(connection),
                    new CarRepository(connection));
            service.runShareCarsEngine();
        } catch (SQLException e) {
            throw new InvalidDatabaseProcessException("Exception during database process: " + e.getMessage());
        }
    }

    private static void initDatabaseTables(Statement statement) throws SQLException {
        //     SqlUtils.dropTableCompanyIfExists(statement);
        SqlUtils.createTableCompany(statement);
        //   SqlUtils.dropTableCarIfExists(statement);
        SqlUtils.createTableCar(statement);
    }
}