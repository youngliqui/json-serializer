package ru.clevertec.deserialization.parser;

import java.util.Map;

public interface JsonParser {
    Map<String, Object> parse(String jsonString);
}
