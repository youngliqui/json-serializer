package ru.clevertec.parser;

import java.util.Map;

public interface JsonParser {
    Map<String, Object> parse(String jsonString);
}
