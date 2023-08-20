package carsharing.repositories;

import carsharing.model.CarDto;

import java.util.List;

public interface CarDao {

    int insertCar(String carName, int companyId);

    List<CarDto> getAllCars(int companyId);

    List<CarDto> getAllCarsPossibleToRent(int companyId);

    CarDto getCar(int rentedCarId);

    int updateIsCarRented(int carId, boolean isCarRented);
}