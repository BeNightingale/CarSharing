package carsharing.repositories;

import carsharing.model.CompanyDto;

import java.util.List;

public interface CompanyDao {

    int insertCompany(String companyName);

    List<CompanyDto> getAllCompanies();

    String getCompanyName(int companyId);
}