package carsharing;

import java.util.List;

public class CompanyDto {

    private Integer id;
    private final String name;
    private List<CarDto> carDtoList;

    public CompanyDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public CompanyDto(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public List<CarDto> getCarDtoList() {
        return carDtoList;
    }

    public String getName() {
        return name;
    }

    public void setCarDtoList(List<CarDto> carDtoList) {
        this.carDtoList = carDtoList;
    }

    @Override
    public String toString() {
        return "CompanyDao{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}