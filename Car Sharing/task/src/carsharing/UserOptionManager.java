package carsharing;

import carsharing.model.CompanyDto;

import java.util.List;
import java.util.Scanner;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class UserOptionManager {
    private final Scanner scanner = new Scanner(System.in);
    static final String START_OPTIONS = String.format(
            "1. Log in as a manager%n" +
                    "2. Log in as a customer%n" +
                    "3. Create a customer%n" +
                    "0. Exit%n"
    );
    static final String ACTION_MANAGER_CHOICES = String.format(
            "1. Company list%n" +
                    "2. Create a company%n" +
                    "0. Back%n"
    );
    static final String ABOUT_CAR_CHOICES = String.format(
            "1. Car list%n" +
                    "2. Create a car%n" +
                    "0. Back%n");
    static final String CUSTOMER_MENU_ABOUT_CAR = String.format(
            "1. Rent a car%n" +
                    "2. Return a rented car%n" +
                    "3. My rented car%n" +
                    "0. Back%n");
    final Predicate<String> fourOptionsValidator = option ->
            option == null || option.isEmpty() || !(List.of("0", "1", "2", "3").contains(option));
    final Predicate<String> threeOptionsValidator = option ->
            option == null || option.isEmpty() || !(List.of("0", "1", "2").contains(option));
    final BiPredicate<String, List<String>> choiceFromListValidator = (option, possibleChoices) ->
            option == null || option.isEmpty() || !possibleChoices.contains(option);

    /**
     * @param selection option list
     * @param validator validates option correctness
     * @return validated decision
     */
    public String getUserDecision(String selection, Predicate<String> validator) {
        System.out.println(selection);
        String option = scanner.nextLine();
        while (validator.test(option)) {
            System.out.println(selection);
            System.out.println("Select number from the list!");
            option = scanner.nextLine();
        }
        return option;
    }

    public <T> String getDecisionAboutList(List<T> list, Consumer<List<T>> consumer, BiPredicate<String, List<String>> validator) {
        final int listSize = list.size();
        final List<String> possibleChoices = IntStream.rangeClosed(0, listSize).boxed().map(Object::toString).toList();
        String option = scanner.nextLine();
        while (validator.test(option, possibleChoices)) {
            consumer.accept(list);
            System.out.println("Select number from the list!");
            option = scanner.nextLine();
        }
        return option;
    }

    public String getDecisionAboutCompany(List<CompanyDto> companyDtoList) {
        return getDecisionAboutList(companyDtoList, Printer.companiesPrinter, choiceFromListValidator);
    }
}