package carsharing;

import java.util.List;

public interface CompanyDao {

    int insertCompany(String companyName);

    List<CompanyDto> getAllCompanies();
}