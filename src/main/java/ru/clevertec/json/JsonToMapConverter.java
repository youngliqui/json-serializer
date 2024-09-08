package ru.clevertec.json;

import ru.clevertec.exception.JsonValidationException;
import ru.clevertec.json.parser.JsonParser;
import ru.clevertec.json.validator.JsonValidator;

import java.util.Map;

public class JsonToMapConverter {
    private JsonValidator jsonValidator;
    private JsonParser jsonParser;

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
