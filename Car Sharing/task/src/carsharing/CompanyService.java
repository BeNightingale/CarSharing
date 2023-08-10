package carsharing;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

import static java.util.Map.entry;

public class CompanyService {

    private final Runnable printAll = this::printAllCompaniesList;
    private final Runnable createCompany = this::addCompanyToDatabase;

    protected final Map<String, Runnable> actionMap = Map.ofEntries(
            entry("1", printAll),
            entry("2", createCompany));
    private final Scanner scanner;
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
        this.scanner = new Scanner(System.in);
    }

    public void printAllCompaniesList() {
        final List<CompanyDto> companyDtoList = companyRepository.getAllCompanies();
        if (companyDtoList == null || companyDtoList.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }
        System.out.println("Company list:");
        int companiesListSize = companyDtoList.size();
        IntStream.rangeClosed(1, companiesListSize).forEach(i -> System.out.printf("%d. %s%n", i, companyDtoList.get(i - 1).getName()));
        System.out.println();
    }

    public void addCompanyToDatabase() {
        System.out.println("Enter the company name:");
        final String companyName = scanner.nextLine();
        final int insertResult = companyRepository.insertCompany(companyName);
        System.out.println(insertResult != -1 ? "The company was created!" : "Error!");
    }
}