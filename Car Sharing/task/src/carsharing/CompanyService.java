package carsharing;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;
import java.util.stream.IntStream;

import static java.util.Map.entry;

public class CompanyService {

    private final Runnable printAll = this::printAllCompaniesList;
    private final Runnable createCompany = this::addCompanyToDatabase;
    private final Consumer<Integer> createCar = this::addCarToDatabase;
    private final Consumer<Integer> printCars = this::printAllCarsForCompany;

    protected final Map<String, Runnable> actionMap = Map.ofEntries(
            entry("1", printAll),
            entry("2", createCompany));

    protected final Map<String, Consumer<Integer>> carActionMap = Map.ofEntries(
            entry("1", printCars),
            entry("2", createCar));

    private final Scanner scanner;
    private final CompanyRepository companyRepository;
    private final CarRepository carRepository;
    private final UserOptionManager userOptionManager = new UserOptionManager();

    public CompanyService(CompanyRepository companyRepository, CarRepository carRepository) {
        this.companyRepository = companyRepository;
        this.carRepository = carRepository;
        this.scanner = new Scanner(System.in);
    }

    public void printAllCompaniesList() {
        final List<CompanyDto> companyDtoList = companyRepository.getAllCompanies();
        if (companyDtoList == null || companyDtoList.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }
        userOptionManager.printAllCompaniesListToChoose(companyDtoList);
    }

    public CompanyRepository getCompanyRepository() {
        return companyRepository;
    }

    public void addCompanyToDatabase() {
        System.out.println("Enter the company name:");
        final String companyName = scanner.nextLine();
        final int insertResult = companyRepository.insertCompany(companyName);
        System.out.println(insertResult != -1 ? "The company was created!" : "Error!");
    }

    public void addCarToDatabase(int companyId) {
        System.out.println("Enter the car name:");
        final String carName = scanner.nextLine();
        final int insertResult = carRepository.insertCar(carName, companyId);
        System.out.println(insertResult != -1 ? "The car was added!" : "Error!");
    }

    public void printAllCarsForCompany(int companyId) {
        final List<CarDto> carsList = carRepository.getAllCars(companyId);
        if (carsList == null || carsList.isEmpty()) {
            System.out.println("The car list is empty!");
            System.out.println();
            return;
        }
        final int carsListSize = carsList.size();
        IntStream.rangeClosed(1, carsListSize).forEach(i -> System.out.printf("%d. %s%n", i, carsList.get(i - 1).getCarName()));
        System.out.println();
    }

    public void runShareCarsEngine() {
        //start decision: login or exit
        String userOption = userOptionManager.getUserDecision(
                UserOptionManager.START_OPTIONS, userOptionManager.startValidator
        );
        // "0"=nothing happens   sth else=enter the loop while
        while (!Objects.equals(userOption, "0")) {
            // read action - create  company, list companies, exit
            String userDecision = userOptionManager.getUserDecision(
                    UserOptionManager.ACTION_CHOICES, userOptionManager.choiceValidator
            );
            while (!Objects.equals(userDecision, "0")) {
                actionMap.get(userDecision).run();
                // if action=list companies
                if ("1".equals(userDecision)) {
                    final List<CompanyDto> companyDtoList = getCompanyRepository().getAllCompanies();
                    // if list is empty=do nothing - exit
                    if (companyDtoList != null && !companyDtoList.isEmpty()) {
                        // if list has companies:
                        // after showing companies user chooses: one company or exit="0"
                        String userCompanyChoice = userOptionManager.getUserDecisionAboutCompany(companyDtoList, userOptionManager.companyChoiceValidator);
                        aboutCompanyDecider.accept(userCompanyChoice, companyDtoList);
                    }
                }
                userDecision = userOptionManager.getUserDecision(
                        UserOptionManager.ACTION_CHOICES, userOptionManager.choiceValidator
                );
            }
            // start decision: log or exit ("0" ends loop while)
            userOption = userOptionManager.getUserDecision(
                    UserOptionManager.START_OPTIONS, userOptionManager.startValidator
            );
        }
    }

    private final BiConsumer<String, List<CompanyDto>> aboutCompanyDecider = this::acceptAboutCompanyDecision;

    private final ObjIntConsumer<String> aboutCarDecider = (userDecisionAboutCar, companyId) -> {
        while (!Objects.equals(userDecisionAboutCar, "0")) {
            carActionMap.get(userDecisionAboutCar).accept(companyId);
            userDecisionAboutCar = userOptionManager.getUserDecision(UserOptionManager.ABOUT_CAR_CHOICES, userOptionManager.choiceValidator);
        }
    };

    private void acceptAboutCompanyDecision(String userCompanyChoice, List<CompanyDto> companyDtoList) {
        while (!Objects.equals(userCompanyChoice, "0")) {
            CompanyDto company = companyDtoList.get(Integer.parseInt(userCompanyChoice) - 1);
            int companyId = company.getId();
            System.out.printf("'%s' company%n", company.getName());
            // for this chosen company car-menu is shown
            // user chooses: create car for this company, show company cars or exit
            String userDecisionAboutCar = userOptionManager.getUserDecision(UserOptionManager.ABOUT_CAR_CHOICES, userOptionManager.choiceValidator);
            aboutCarDecider.accept(userDecisionAboutCar, companyId);
            userCompanyChoice = "0";
        }
    }
}