package carsharing;

import carsharing.model.CarDto;
import carsharing.model.CompanyDto;
import carsharing.model.CustomerDto;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class Printer {

    private static final String BACK = "0. Back";
    private static final String PRINT_ELEMENT = "%d. %s%n";

    private Printer() {
        //nth
    }

    protected static final Consumer<List<CarDto>> carsPrinterForCustomer = carsList -> {
        System.out.println("Choose a car:");
        final Integer carsListSize = carsList.size();
        IntStream.rangeClosed(1, carsListSize).forEach(i -> System.out.printf(PRINT_ELEMENT, i, carsList.get(i - 1).getCarName()));
        System.out.println(BACK);
        System.out.println();
    };

    protected static final Consumer<List<CarDto>> carsPrinterForCompany = carsList -> {
        System.out.println("Car list:");
        final int carsListSize = carsList.size();
        IntStream.rangeClosed(1, carsListSize).forEach(i -> System.out.printf(PRINT_ELEMENT, i, carsList.get(i - 1).getCarName()));
        System.out.println();
    };

    protected static final Consumer<List<CompanyDto>> companiesPrinter = companiesList -> {
        System.out.println("Choose a company:");
        final int companiesListSize = companiesList.size();
        IntStream.rangeClosed(1, companiesListSize).forEach(i -> System.out.printf(PRINT_ELEMENT, i, companiesList.get(i - 1).getName()));
        System.out.println(BACK);
        System.out.println();
    };

    protected static final Consumer<List<CustomerDto>> customersPrinter = customersList -> {
        System.out.println("The customer list:");
        final int customersListSize = customersList.size();
        IntStream.rangeClosed(1, customersListSize).forEach(i -> System.out.printf(PRINT_ELEMENT, i, customersList.get(i - 1).getCustomerName()));
        System.out.println(BACK);
        System.out.println();
    };
}
