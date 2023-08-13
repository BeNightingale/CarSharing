package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarRepository implements CarDao {

    private final Connection connection;

    public CarRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int insertCar(String carName, int companyId) {
        if (carName == null || carName.isEmpty()) {
            return -1;
        }
        final String insertIntoCarTableSql = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertIntoCarTableSql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, carName);
            preparedStatement.setInt(2, companyId);
            preparedStatement.executeUpdate();
            //Pobranie wygenerowanego klucza gotową metodą getGeneratedKeys()!!!
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next())
                return resultSet.getInt(1); // Statement.RETURN_GENERATED_KEYS this flag allows to take the generated key
        } catch (SQLException | NullPointerException ex) {
            System.out.println("Error during inserting new car into database" + ex);
            return -1;
        }
        return -1;
    }

    @Override
    public List<CarDto> getAllCars(int companyId) {
        final String selectSql = "SELECT * FROM CAR WHERE COMPANY_ID = ? ORDER BY ID";
        final List<CarDto> carDtoList = new ArrayList<>();
        try (final PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
            preparedStatement.setInt(1, companyId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                final CarDto carDto = new CarDto(id, name, companyId);
                carDtoList.add(carDto);
            }
        } catch (SQLException sqlEx) {
            System.out.printf("No success in searching for all cars in database for company with id=%d, %s", companyId, sqlEx);
            return Collections.emptyList();
        }
        return carDtoList;
    }
}