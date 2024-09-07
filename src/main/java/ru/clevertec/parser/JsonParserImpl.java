package ru.clevertec.parser;

import ru.clevertec.handler.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JsonParserImpl implements JsonParser {
    private Map<String, JsonValueHandler> handlers;
    private Map<Predicate<String>, Function<String, Object>> handlersActions;

    public JsonParserImpl() {
        handlers = Map.of(
                "int", new IntHandler(),
                "boolean", new BooleanHandler(),
                "string", new StringHandler(),
                "object", new ObjectHandler(this)
        );
        handlersActions = Map.of(
                val -> val.startsWith("{") && val.endsWith("}"), val -> handlers.get("object").handle(val),
                val -> val.startsWith("[") && val.endsWith("]"), this::parseJsonArray,
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
            if (c == '{') {
                braceCount++;
            } else if (c == '}') {
                braceCount--;
            } else if (c == '[') {
                bracketCount++;
            } else if (c == ']') {
                bracketCount--;
            } else if (c == ',' && braceCount == 0 && bracketCount == 0) {
                pairs.add(currentPair.toString().trim());
                currentPair.setLength(0);
                continue;
            }
            currentPair.append(c);
        }

        if (currentPair.length() > 0) {
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
        return parseStringValue(pair.split(":")[0].trim());
    }

    private Object parseValue(String pair) {
        int colonIndex = pair.indexOf(':');
        String value = pair.substring(colonIndex + 1).trim();
        return parseJsonValue(value);
    }

    private String parseStringValue(String value) {
        return Optional.of(value)
                .filter(s -> s.startsWith("\"") && s.endsWith("\""))
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
        jsonArray = jsonArray.substring(1, jsonArray.length() - 1).trim();
//        return Stream.of(jsonArray.split(","))
//                .map(String::trim)
//                .map(this::parseJsonValue)
//                .collect(Collectors.toList());
        return splitPairs(jsonArray).stream()
                .map(this::parseJsonValue)
                .collect(Collectors.toList());
    }

    private Optional<String> parseJsonString(String jsonString) {
        return Optional.ofNullable(jsonString)
                .map(String::trim)
                .filter(s -> s.startsWith("{") && s.endsWith("}"))
                .map(s -> s.substring(1, s.length() - 1));
    }
}
