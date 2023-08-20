package carsharing.repositories;

import carsharing.model.CompanyDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompanyRepository implements CompanyDao {

    private final Connection connection;

    public CompanyRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int insertCompany(String companyName) {
        if (companyName == null || companyName.isEmpty()) {
            return -1;
        }
        final String insertIntoCompanyTableSql = "INSERT INTO COMPANY (NAME) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertIntoCompanyTableSql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, companyName);
            preparedStatement.executeUpdate();
            // Getting generated key with method getGeneratedKeys()!
            final ResultSet resultSet = preparedStatement.getGeneratedKeys();
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
                final Integer id = resultSet.getInt(1);
                final String name = resultSet.getString(2);
                final CompanyDto companyDto = new CompanyDto(id, name);
                companyDtoList.add(companyDto);
            }
        } catch (SQLException sqlEx) {
            System.out.println("No success in searching for all companies in database, " + sqlEx);
            return Collections.emptyList();
        }
        return companyDtoList;
    }

    @Override
    public String getCompanyName(int companyId) {
        final String selectSql = "SELECT NAME FROM COMPANY WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
            preparedStatement.setInt(1, companyId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString(1);
        } catch (SQLException sqlEx) {
            System.out.printf("No success in searching for a company with id = %s, %s%n", companyId, sqlEx);
            return null;
        }
    }
}