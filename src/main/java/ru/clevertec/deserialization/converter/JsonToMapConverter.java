package ru.clevertec.deserialization.converter;

import ru.clevertec.exception.JsonValidationException;
import ru.clevertec.deserialization.parser.JsonParser;
import ru.clevertec.deserialization.validator.JsonValidator;

import java.util.Map;

public class JsonToMapConverter {
    private final JsonValidator jsonValidator;
    private final JsonParser jsonParser;

    public JsonToMapConverter(JsonValidator jsonValidator, JsonParser jsonParser) {
        this.jsonValidator = jsonValidator;
        this.jsonParser = jsonParser;
    }

    public Map<String, Object> convertJsonToMap(String jsonString) {
        if (!jsonValidator.validate(jsonString)) {
            throw new JsonValidationException();
        }

        return jsonParser.parse(jsonString);
    }
}
