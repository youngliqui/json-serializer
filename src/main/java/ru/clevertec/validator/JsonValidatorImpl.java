package ru.clevertec.validator;

import java.util.Optional;

public class JsonValidatorImpl implements JsonValidator {

    @Override
    public boolean validate(String json) {
        return Optional.ofNullable(json)
                .map(String::trim)
                .filter(j -> !j.isEmpty())
                .filter(j -> j.startsWith("{") && j.endsWith("}"))
                .filter(this::hasBalancedBraces)
                .isPresent();
    }

    private boolean hasBalancedBraces(String json) {
        return json.chars()
                .reduce(0, (count, c) -> {
                    if (c == '{') return ++count;
                    if (c == '}') return --count;

                    return count;
                }) == 0;
    }
}
