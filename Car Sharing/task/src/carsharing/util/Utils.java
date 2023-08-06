package carsharing.util;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static java.util.Map.entry;

public class Utils {

    private Utils() {
        // sth
    }

    public static String getFirstArgsParameter(String[] args, String parameterName, String defaultValue) {
        final int argsLength = args.length;
        final Supplier<String> paramSpecifier0 = () -> defaultValue;
        final Supplier<String> paramSpecifier2 = () -> {
            final String parameterValue = args[1];
            if (Objects.equals(parameterName, args[0]) && parameterValue != null && !parameterValue.isEmpty())
                return parameterValue;
            return defaultValue;
        };
        final Map<Integer, Supplier<String>> argsMap = Map.ofEntries(
                entry(0, paramSpecifier0),
                entry(1, paramSpecifier0),
                entry(2, paramSpecifier2)
        );
        return argsMap.get(argsLength).get();
    }
}