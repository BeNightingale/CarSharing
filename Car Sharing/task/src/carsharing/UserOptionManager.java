package carsharing;

import java.util.List;
import java.util.Scanner;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class UserOptionManager {
    private final Scanner scanner = new Scanner(System.in);
    static final String START_OPTIONS = String.format("1. Log in as a manager%n0. Exit%n");
    static final String ACTION_CHOICES = String.format("1. Company list%n2. Create a company%n0. Back%n");
    static final String ABOUT_CAR_CHOICES = String.format("1. Car list%n2. Create a car%n0. Back");
    final Predicate<String> startValidator = option ->
            option == null || option.isEmpty() || !(List.of("0", "1").contains(option));
    final Predicate<String> choiceValidator = option ->
            option == null || option.isEmpty() || !(List.of("0", "1", "2").contains(option));
    final BiPredicate<String, List<String>> companyChoiceValidator = (option, possibleChoices) ->
            option == null || option.isEmpty() || !possibleChoices.contains(option);

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

    public String getUserDecisionAboutCompany(List<CompanyDto> companyDtoList, BiPredicate<String, List<String>> validator) {
        final int companiesSize = companyDtoList.size();
        final List<String> possibleChoices = IntStream.rangeClosed(0, companiesSize).boxed().map(Object::toString).toList();
        String option = scanner.nextLine();
        while (validator.test(option, possibleChoices)) {
            printAllCompaniesListToChoose(companyDtoList);
            System.out.println("Select number from the list!");
            option = scanner.nextLine();
        }
        return option;
    }

    public void printAllCompaniesListToChoose(List<CompanyDto> companyDtoList) {
        System.out.println("Choose the company:");
        final int companiesListSize = companyDtoList.size();
        IntStream.rangeClosed(1, companiesListSize).forEach(i -> System.out.printf("%d. %s%n", i, companyDtoList.get(i - 1).getName()));
        System.out.println("0. Back");
    }
}