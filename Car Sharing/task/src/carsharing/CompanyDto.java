package carsharing;

public class CompanyDto {
    private Integer id;
    private final String name;

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

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CompanyDao{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}