package ru.clevertec.deserialization.parser;

import ru.clevertec.handler.valuehandlers.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.clevertec.constants.CharacterConstants.*;

public class JsonParserImpl implements JsonParser {
    private final Map<String, JsonValueHandler> handlers;
    private final Map<Predicate<String>, Function<String, Object>> handlersActions;

    public JsonParserImpl() {
        handlers = Map.of(
                "int", new IntHandler(),
                "boolean", new BooleanHandler(),
                "string", new StringHandler(),
                "object", new ObjectHandler(this)
        );
        handlersActions = Map.of(
                val -> val.startsWith(OPEN_CURLY_BRACKET) && val.endsWith(CLOSE_CURLY_BRACKET), val -> handlers.get("object").handle(val),
                val -> val.startsWith(OPEN_SQUARE_BRACKET) && val.endsWith(CLOSE_SQUARE_BRACKET), this::parseJsonArray,
                val -> val.matches("-?\\d+"), val -> handlers.get("int").handle(val),
                val -> val.matches("true|false"), val -> handlers.get("boolean").handle(val)
        );
    }

    @Override
    public Map<String, Object> parse(String jsonString) {
        return parseJsonString(jsonString)
                .map(this::splitPairs)
                .map(this::parsePairs)
                .orElse(Collections.emptyMap());
    }

    private List<String> splitPairs(String jsonString) {
        List<String> pairs = new ArrayList<>();
        StringBuilder currentPair = new StringBuilder();
        int braceCount = 0;
        int bracketCount = 0;

        for (char c : jsonString.toCharArray()) {
            if (c == OPEN_CURLY_BRACKET.charAt(0)) {
                braceCount++;
            } else if (c == CLOSE_CURLY_BRACKET.charAt(0)) {
                braceCount--;
            } else if (c == OPEN_SQUARE_BRACKET.charAt(0)) {
                bracketCount++;
            } else if (c == CLOSE_SQUARE_BRACKET.charAt(0)) {
                bracketCount--;
            } else if (c == COMMA.charAt(0) && braceCount == 0 && bracketCount == 0) {
                pairs.add(currentPair.toString().trim());
                currentPair.setLength(0);
                continue;
            }
            currentPair.append(c);
        }

        if (!currentPair.isEmpty()) {
            pairs.add(currentPair.toString().trim());
        }

        return pairs;
    }

    private Map<String, Object> parsePairs(List<String> pairs) {
        return pairs.stream().collect(Collectors.toMap(
                this::parseKey,
                this::parseValue
        ));
    }

    private String parseKey(String pair) {
        return parseStringValue(pair.split(COLON)[0].trim());
    }

    private Object parseValue(String pair) {
        int colonIndex = pair.indexOf(COLON);
        String value = pair.substring(colonIndex + 1).trim();
        return parseJsonValue(value);
    }

    private String parseStringValue(String value) {
        return Optional.of(value)
                .filter(s -> s.startsWith(ESCAPED_QUOTE) && s.endsWith(ESCAPED_QUOTE))
                .map(s -> s.substring(1, s.length() - 1))
                .orElse(value);
    }

    private Object parseJsonValue(String value) {
        return handlersActions.entrySet()
                .stream()
                .filter(entry -> entry.getKey().test(value))
                .findFirst()
                .map(Map.Entry::getValue)
                .map(val -> val.apply(value))
                .orElseGet(() -> handlers.get("string").handle(value));
    }

    private List<Object> parseJsonArray(String jsonArray) {
        String trimmedJsonArray = jsonArray.substring(1, jsonArray.length() - 1).trim();
        return splitPairs(trimmedJsonArray).stream()
                .map(this::parseJsonValue)
                .collect(Collectors.toList());
    }

    private Optional<String> parseJsonString(String jsonString) {
        return Optional.ofNullable(jsonString)
                .map(String::trim)
                .filter(s -> s.startsWith(OPEN_CURLY_BRACKET) && s.endsWith(CLOSE_CURLY_BRACKET))
                .map(s -> s.substring(1, s.length() - 1));
    }
}
