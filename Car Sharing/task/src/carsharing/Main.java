package carsharing;

import carsharing.repositories.CarRepository;
import carsharing.repositories.CompanyRepository;
import carsharing.repositories.CustomerRepository;
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

        final String URL = "jdbc:h2:./src/carsharing/db/" + databaseFileName + ";DB_CLOSE_DELAY=-1";

        try (Connection connection = DriverManager.getConnection(URL);
             final Statement statement = connection.createStatement()
        ) {
            connection.setAutoCommit(true);
            initDatabaseTables(statement);
            final Service service = new Service(
                    new CompanyRepository(connection),
                    new CarRepository(connection),
                    new CustomerRepository(connection));
            service.runShareCarsEngine();
        } catch (SQLException e) {
            throw new InvalidDatabaseProcessException("Exception during database process: " + e.getMessage());
        }
    }

    private static void initDatabaseTables(Statement statement) throws SQLException {
        // SqlUtils.dropTableCompanyIfExists(statement);
        SqlUtils.createTableCompany(statement);
        // SqlUtils.dropTableCarIfExists(statement);
        SqlUtils.createTableCar(statement);
     //    SqlUtils.alterTableCarAddColumn(statement);
        SqlUtils.createTableCustomer(statement);
        // SqlUtils.alterTableCustomer(statement);
    }
}