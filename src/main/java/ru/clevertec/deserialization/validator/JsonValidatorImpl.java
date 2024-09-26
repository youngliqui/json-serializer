package ru.clevertec.deserialization.validator;

import java.util.Optional;

import static ru.clevertec.constants.CharacterConstants.CLOSE_CURLY_BRACKET;
import static ru.clevertec.constants.CharacterConstants.OPEN_CURLY_BRACKET;

public class JsonValidatorImpl implements JsonValidator {

    @Override
    public boolean validate(String json) {
        return Optional.ofNullable(json)
                .map(String::trim)
                .filter(j -> !j.isEmpty())
                .filter(j -> j.startsWith(OPEN_CURLY_BRACKET) && j.endsWith(CLOSE_CURLY_BRACKET))
                .filter(this::hasBalancedBraces)
                .isPresent();
    }

    private boolean hasBalancedBraces(String json) {
        return json.chars()
                .reduce(0, (count, c) -> {
                    if (c == OPEN_CURLY_BRACKET.charAt(0)) return ++count;
                    if (c == CLOSE_CURLY_BRACKET.charAt(0)) return --count;

                    return count;
                }) == 0;
    }
}
