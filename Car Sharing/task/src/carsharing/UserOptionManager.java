package carsharing;

import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class UserOptionManager {
    private final Scanner scanner = new Scanner(System.in);
    static final String START_OPTIONS = String.format("1. Log in as a manager%n0. Exit%n");
    static final String ACTION_CHOICES = String.format("1. Company list%n2. Create a company%n0. Back%n");

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

    final Predicate<String> startValidator = option -> option == null || option.isEmpty() || !(List.of("0", "1").contains(option));

    final Predicate<String> choiceValidator = option -> option == null || option.isEmpty() || !(List.of("0", "1", "2").contains(option));
}