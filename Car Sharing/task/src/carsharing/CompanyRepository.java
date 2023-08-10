package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompanyRepository implements CompanyDao {
    private final Connection connection;

    public CompanyRepository(Connection connection) {
        this.connection = connection;
    }

    // returns the key (id) of inserted company
    protected int insertCompany(CompanyDto companyDto) {
        if (companyDto == null) {
            return -1;
        }
        final String insertIntCompanyTableSql = "INSERT INTO COMPANY (NAME) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertIntCompanyTableSql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, companyDto.getName());

            // final ResultSet resultSet = preparedStatement.executeQuery();
            preparedStatement.executeUpdate();
            //Pobranie wygenerowanego klucza gotową metodą getGeneratedKeys()!!!
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next())
                return resultSet.getInt(1); // Statement.RETURN_GENERATED_KEYS this flag allows to take the generated key
        } catch (SQLException | NullPointerException ex) {
            System.out.println("Error " + ex);
            return -1;
        }
        return -1;
    }

    @Override
    public int insertCompany(String companyName) {
        if (companyName == null || companyName.isEmpty()) {
            return -1;
        }
        final String insertIntCompanyTableSql = "INSERT INTO COMPANY (NAME) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertIntCompanyTableSql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, companyName);

            // final ResultSet resultSet = preparedStatement.executeQuery();
            preparedStatement.executeUpdate();
            //Pobranie wygenerowanego klucza gotową metodą getGeneratedKeys()!!!
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next())
                return resultSet.getInt(1); // Statement.RETURN_GENERATED_KEYS this flag allows to take the generated key
        } catch (SQLException | NullPointerException ex) {
            System.out.println("Error " + ex);
            return -1;
        }
        return -1;
    }

    @Override
    public List<CompanyDto> getAllCompanies() {
        final String selectSql = "SELECT * FROM COMPANY ORDER BY ID";
        final List<CompanyDto> companyDtoList = new ArrayList<>();
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(selectSql);
            while (resultSet.next()) {
                Integer id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                final CompanyDto companyDto = new CompanyDto(id, name);
                companyDtoList.add(companyDto);
            }
        } catch (SQLException sqlEx) {
            System.out.println("No success in searching for all companies in database, " + sqlEx);
            return Collections.emptyList();
        }
        return companyDtoList;
    }
}