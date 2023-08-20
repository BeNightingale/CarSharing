package carsharing.model;

public class CarDto {

    private final Integer id;
    private final String carName;
    private final int companyId;

    public CarDto(Integer id, String carName, int companyId) {
        this.id = id;
        this.carName = carName;
        this.companyId = companyId;
    }

    public int getId() {
        return id;
    }

    public String getCarName() {
        return carName;
    }

    public int getCompanyId() {
        return companyId;
    }
}