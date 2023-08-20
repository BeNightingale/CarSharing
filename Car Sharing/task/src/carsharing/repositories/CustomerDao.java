package carsharing.repositories;

import carsharing.model.CustomerDto;

import java.util.List;

public interface CustomerDao {

    int insertCustomer(String customerName);

    List<CustomerDto> getAllCustomers();

    CustomerDto getCustomerById(int customerId);

    int rentCar(int customerId, int carId);

    int returnCar(int customerId);
}