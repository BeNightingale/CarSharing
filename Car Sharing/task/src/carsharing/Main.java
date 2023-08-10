package carsharing;

import carsharing.util.SqlUtils;
import carsharing.util.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;


public class Main {
    private static final UserOptionManager USER_OPTION_MANAGER = new UserOptionManager();

    public static void main(String[] args) {

        final String databaseFileName = Utils.getFirstArgsParameter(args, "-databaseFileName", "carsharing");
        System.out.println(databaseFileName);

        final String URL = "jdbc:h2:./src/carsharing/db/" + databaseFileName;

        try (Connection connection = DriverManager.getConnection(URL);
             final Statement statement = connection.createStatement()
        ) {
            connection.setAutoCommit(true);
            SqlUtils.dropTableCompanyIfExists(statement);
            SqlUtils.createTableCompany(statement);
            final CompanyRepository companyRepository = new CompanyRepository(connection);
            final CompanyService service = new CompanyService(companyRepository);
            String userOption = USER_OPTION_MANAGER.getUserDecision(
                    UserOptionManager.START_OPTIONS, USER_OPTION_MANAGER.startValidator
            );
            while (!Objects.equals(userOption, "0")) {
                String userDecision = USER_OPTION_MANAGER.getUserDecision(
                        UserOptionManager.ACTION_CHOICES, USER_OPTION_MANAGER.choiceValidator
                );
                while (!Objects.equals(userDecision, "0")) {
                    service.actionMap.get(userDecision).run();
                    userDecision = USER_OPTION_MANAGER.getUserDecision(
                            UserOptionManager.ACTION_CHOICES, USER_OPTION_MANAGER.choiceValidator
                    );
                }
                userOption = USER_OPTION_MANAGER.getUserDecision(
                        UserOptionManager.START_OPTIONS, USER_OPTION_MANAGER.startValidator
                );
            }
        } catch (SQLException e) {
            throw new InvalidDatabaseProcessException("Exception during database process: " + e.getMessage());
        }
    }
}