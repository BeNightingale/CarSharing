package carsharing.repositories;

import carsharing.model.CustomerDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerRepository implements CustomerDao {

    private final Connection connection;

    public CustomerRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int insertCustomer(String customerName) {
        if (customerName == null || customerName.isEmpty())
            return -1;
        final String insertIntoCustomerTableSql = "INSERT INTO CUSTOMER (NAME) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertIntoCustomerTableSql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, customerName);
            preparedStatement.executeUpdate();
            // Getting generated key with method getGeneratedKeys()!
            final ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next())
                return resultSet.getInt(1); // Statement.RETURN_GENERATED_KEYS this flag allows to take the generated key
        } catch (SQLException | NullPointerException ex) {
            System.out.println("Error during inserting new customer into database" + ex);
            return -1;
        }
        return -1;
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        final String selectCustomersSql = "SELECT * FROM CUSTOMER ORDER BY ID";
        try (Statement statement = connection.createStatement()) {
            final List<CustomerDto> customersList = new ArrayList<>();
            final ResultSet resultSet = statement.executeQuery(selectCustomersSql);
            while (resultSet.next()) {
                final CustomerDto customerDto = parseResultSetDataToCustomer(resultSet);
                customersList.add(customerDto);
            }
            return customersList;
        } catch (SQLException e) {
            System.out.println("Error during searching for all customers information in database " + e);
            return Collections.emptyList();
        }
    }

    @Override
    public CustomerDto getCustomerById(int customerId) {
        final String selectCustomerSql = "SELECT * FROM CUSTOMER WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectCustomerSql)) {
            preparedStatement.setInt(1, customerId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return parseResultSetDataToCustomer(resultSet);
        } catch (SQLException sqlEx) {
            System.out.printf("Error during searching for a customer with id = %s, %s%n", customerId, sqlEx);
            return null;
        }
    }

    @Override
    public int rentCar(int customerId, int carId) {
        final String updateCustomerSql = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateCustomerSql);
        ) {
            preparedStatement.setInt(1, carId);
            preparedStatement.setInt(2, customerId);
            return preparedStatement.executeUpdate(); // changed rows number
        } catch (SQLException ex) {
            System.out.println("Error during updating customer information in database (returning a car)" + ex);
            return 0;
        }
    }

    @Override
    public int returnCar(int customerId) {
        final String updateCustomerSql = "UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateCustomerSql);
        ) {
            preparedStatement.setInt(1, customerId);
            return preparedStatement.executeUpdate(); // changed rows number
        } catch (SQLException ex) {
            System.out.println("Error during updating customer information in database (returning a car)" + ex);
            return 0;
        }
    }

    private CustomerDto parseResultSetDataToCustomer(ResultSet resultSet) throws SQLException {
        int customerId = resultSet.getInt(1);
        final String customerName = resultSet.getString(2);
        final Object object = resultSet.getObject(3);
        final Integer rentedCarId = object == null ? null : (Integer) object;
        return new CustomerDto(customerId, customerName, rentedCarId);
    }
}