package carsharing;

import carsharing.model.CarDto;
import carsharing.model.CompanyDto;
import carsharing.model.CustomerDto;
import carsharing.repositories.CarRepository;
import carsharing.repositories.CompanyRepository;
import carsharing.repositories.CustomerRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

import static java.util.Map.entry;

public class Service {


    private final Runnable createCustomer = this::addCustomerToDatabase;
    private final Runnable logManager = this::logInManager;
    private final Runnable logCustomer = this::logInCustomer;
    private final Runnable printAll = this::printAllCompanies;
    private final Runnable createCompany = this::addCompanyToDatabase;
    private final Consumer<Integer> createCar = this::addCarToDatabase;
    private final Consumer<Integer> printCars = this::printAllCarsForCompany;
    private final Consumer<CustomerDto> returnCar = this::returnCarForCustomer;
    private final Consumer<CustomerDto> rentCar = this::rentCarForCustomer;
    private final Consumer<CustomerDto> showRentedCar = this::showRentedCarForCustomer;

    protected final Map<String, Runnable> startActionMap = Map.ofEntries(
            entry("1", logManager),
            entry("2", logCustomer),
            entry("3", createCustomer));

    protected final Map<String, Runnable> actionMap = Map.ofEntries(
            entry("1", printAll),
            entry("2", createCompany));

    protected final Map<String, Consumer<Integer>> carActionMap = Map.ofEntries(
            entry("1", printCars),
            entry("2", createCar));
    protected final Map<String, Consumer<CustomerDto>> customerDecisionAboutCarMap = Map.ofEntries(
            entry("1", rentCar),
            entry("2", returnCar),
            entry("3", showRentedCar));

    private final Scanner scanner;
    private final CompanyRepository companyRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final UserOptionManager userOptionManager = new UserOptionManager();

    public Service(CompanyRepository companyRepository, CarRepository carRepository, CustomerRepository customerRepository) {
        this.companyRepository = companyRepository;
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
        this.scanner = new Scanner(System.in);
    }

    public CompanyRepository getCompanyRepository() {
        return companyRepository;
    }

    public void runShareCarsEngine() {
        //start decision: login or exit
        String userOption = userOptionManager.getUserDecision(
                UserOptionManager.START_OPTIONS, userOptionManager.fourOptionsValidator
        );
        // "0"=nothing happens   sth else=enter the loop while
        while (!Objects.equals(userOption, "0")) {
            startActionMap.get(userOption).run(); // no repeating
            // start decision: log or exit ("0" ends loop while)
            userOption = userOptionManager.getUserDecision(
                    UserOptionManager.START_OPTIONS, userOptionManager.fourOptionsValidator
            );
        }
    }

    private void addCompanyToDatabase() {
        System.out.println("Enter the company name:");
        final String companyName = scanner.nextLine();
        final int insertResult = companyRepository.insertCompany(companyName);
        System.out.println(insertResult != -1 ? "The company was created!" : "Error during adding a new company!!");
        System.out.println();
    }

    private void addCarToDatabase(int companyId) {
        System.out.println("Enter the car name:");
        final String carName = scanner.nextLine();
        final int insertResult = carRepository.insertCar(carName, companyId);
        System.out.println(insertResult != -1 ? "The car was added!" : "Error!");
        System.out.println();
    }

    private void addCustomerToDatabase() {
        System.out.println("Enter the customer name:");
        final String customerName = scanner.nextLine();
        final int insertResult = customerRepository.insertCustomer(customerName);
        System.out.println(insertResult != -1 ? "The customer was added!" : "Error during adding a new customer!");
        System.out.println();
    }

    private void logInManager() {
        // read action - create  company, list companies, exit
        String managerDecision = userOptionManager.getUserDecision(
                UserOptionManager.ACTION_MANAGER_CHOICES, userOptionManager.threeOptionsValidator
        );
        while (!Objects.equals(managerDecision, "0")) {
            actionMap.get(managerDecision).run();
            // if action=list companies
            if ("1".equals(managerDecision)) {
                final List<CompanyDto> companyDtoList = getCompanyRepository().getAllCompanies();
                // if list is empty=do nothing - exit
                if (companyDtoList != null && !companyDtoList.isEmpty()) {
                    // if list has companies:
                    // after showing companies user chooses: one company or exit="0"
                    String userCompanyChoice = userOptionManager.getDecisionAboutCompany(companyDtoList);
                    aboutCompanyDecider.accept(userCompanyChoice, companyDtoList);
                }
            }
            managerDecision = userOptionManager.getUserDecision(
                    UserOptionManager.ACTION_MANAGER_CHOICES, userOptionManager.threeOptionsValidator
            );
        }
    }

    private void logInCustomer() {
        final List<CustomerDto> customersList = customerRepository.getAllCustomers();
        printList("customer", customersList, Printer.customersPrinter);
        if (customersList == null || customersList.isEmpty()) {
            return;
        }
        String customerDecision = userOptionManager.getDecisionAboutList(
                customersList, Printer.customersPrinter, userOptionManager.choiceFromListValidator);
        while (!Objects.equals(customerDecision, "0")) {
            final CustomerDto customer = customersList.get(Integer.parseInt(customerDecision) - 1);
            // print menu about cars
            String customerDecisionAboutCar = userOptionManager.getUserDecision(
                    UserOptionManager.CUSTOMER_MENU_ABOUT_CAR, userOptionManager.fourOptionsValidator);
            while (!Objects.equals(customerDecisionAboutCar, "0")) {
                customerDecisionAboutCarMap.get(customerDecisionAboutCar).accept(customer);
                customerDecisionAboutCar = userOptionManager.getUserDecision(
                        UserOptionManager.CUSTOMER_MENU_ABOUT_CAR, userOptionManager.fourOptionsValidator);
            }
            customerDecision = "0";
        }
    }

    private void rentCarForCustomer(CustomerDto customerDto) {
        if (customerDto.getRentedCarId() != null) {
            System.out.println("You've already rented a car!");
            return;
        }
        final List<CompanyDto> companyDtoList = getCompanyRepository().getAllCompanies();
        Printer.companiesPrinter.accept(companyDtoList);
        final String companyChoice = scanner.nextLine();
        if (Objects.equals("0", companyChoice))
            return;
        final CompanyDto companyDto = companyDtoList.get(Integer.parseInt(companyChoice) - 1);

        final List<CarDto> companyCarList = carRepository.getAllCarsPossibleToRent(companyDto.getId());
        if (companyCarList == null || companyCarList.isEmpty()) {
            System.out.println("Car list is empty!");
            return;
        }
        Printer.carsPrinterForCustomer.accept(companyCarList);
        final String carChoice = scanner.nextLine();
        if (Objects.equals("0", carChoice))
            return;
        final CarDto carDto = companyCarList.get(Integer.parseInt(carChoice) - 1);
        final int rentResult = customerRepository.rentCar(customerDto.getId(), carDto.getId());
        final int updatedRowsNum = carRepository.updateIsCarRented(carDto.getId(), true);
        customerDto.setRentedCarId(carDto.getId());
        System.out.println((rentResult != 1 || updatedRowsNum != 1) ?
                "Error! Car not rented!" :
                String.format("You rented '%s'", carDto.getCarName()));
    }

    private void returnCarForCustomer(CustomerDto customerDto) {
        final int customerId = customerDto.getId();
        Integer rentedCarId = customerDto.getRentedCarId();
        if (rentedCarId == null) {
            System.out.println("You didn't rent a car!");
            return;
        }
        final int changedRowsNumber = customerRepository.returnCar(customerId);
        final int updatedRowsNum = carRepository.updateIsCarRented(rentedCarId, false);
        if (changedRowsNumber != 0 && updatedRowsNum != 0) {
            customerDto.setRentedCarId(null);
            System.out.println("You've returned a rented car!");
            return;
        }
        System.out.println("It wasn't possible to return a car, error!");
    }

    private void showRentedCarForCustomer(CustomerDto customerDto) {
        final CustomerDto customer = customerRepository.getCustomerById(customerDto.getId());
        final Integer rentedCarId = customer.getRentedCarId();
        if (rentedCarId == null) {
            System.out.println("You didn't rent a car!");
            return;
        }
        final CarDto carDto = carRepository.getCar(rentedCarId);
        final String carDtoName = carDto.getCarName();
        final int companyId = carDto.getCompanyId();
        final String companyDtoName = companyRepository.getCompanyName(companyId);
        System.out.printf("Your rented car:%n%s%nCompany:%n%s%n", carDtoName, companyDtoName);
    }

    private void printAllCarsForCompany(int companyId) {
        final List<CarDto> carsList = carRepository.getAllCars(companyId);
        printList("car", carsList, Printer.carsPrinterForCompany);
    }

    private void printAllCompanies() {
        final List<CompanyDto> companiesList = companyRepository.getAllCompanies();
        printList("company", companiesList, Printer.companiesPrinter);
    }

    private <T> void printList(String elementType, List<T> list, Consumer<List<T>> consumer) {
        if (list == null || list.isEmpty()) {
            System.out.printf("The %s list is empty!", elementType);
            System.out.println();
            return;
        }
        consumer.accept(list);
        System.out.println();
    }

    private final BiConsumer<String, List<CompanyDto>> aboutCompanyDecider = this::acceptAboutCompanyDecision;

    private final ObjIntConsumer<String> aboutCarDecider = (userDecisionAboutCar, companyId) -> {
        while (!Objects.equals(userDecisionAboutCar, "0")) {
            carActionMap.get(userDecisionAboutCar).accept(companyId);
            userDecisionAboutCar = userOptionManager.getUserDecision(UserOptionManager.ABOUT_CAR_CHOICES, userOptionManager.threeOptionsValidator);
        }
    };

    private void acceptAboutCompanyDecision(String userCompanyChoice, List<CompanyDto> companyDtoList) {
        while (!Objects.equals(userCompanyChoice, "0")) {
            CompanyDto company = companyDtoList.get(Integer.parseInt(userCompanyChoice) - 1);
            int companyId = company.getId();
            System.out.printf("'%s' company%n", company.getName());
            // for this chosen company car-menu is shown
            // user chooses: create car for this company, show company cars or exit
            String userDecisionAboutCar = userOptionManager.getUserDecision(UserOptionManager.ABOUT_CAR_CHOICES, userOptionManager.threeOptionsValidator);
            aboutCarDecider.accept(userDecisionAboutCar, companyId);
            userCompanyChoice = "0";
        }
    }
}