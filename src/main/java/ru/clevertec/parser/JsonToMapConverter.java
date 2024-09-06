package ru.clevertec.parser;

import ru.clevertec.exception.JsonValidationException;
import ru.clevertec.validator.JsonValidator;
import ru.clevertec.validator.JsonValidatorImpl;

import java.util.Map;

public class JsonToMapConverter {
    private JsonValidator jsonValidator;
    private JsonParser jsonParser;

    public JsonToMapConverter() {
        jsonValidator = new JsonValidatorImpl();
        jsonParser = new JsonParserImpl();
    }

    public Map<String, Object> convertJsonToMap(String jsonString) {
        if (!jsonValidator.validate(jsonString)) {
            throw new JsonValidationException();
        }

        return jsonParser.parse(jsonString);
    }
}
