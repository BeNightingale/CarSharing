package carsharing.model;

public class CustomerDto {

    private final int id;
    private final String customerName;
    private Integer rentedCarId;

    public CustomerDto(int id, String customerName, Integer rentedCarId) {
        this.id = id;
        this.customerName = customerName;
        this.rentedCarId = rentedCarId;
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    public void setRentedCarId(Integer rentedCarId) {
        this.rentedCarId = rentedCarId;
    }
}
