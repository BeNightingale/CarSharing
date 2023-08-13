package carsharing;

import java.util.List;

public interface CarDao {

    int insertCar(String carName, int companyId);

    List<CarDto> getAllCars(int companyId);
}