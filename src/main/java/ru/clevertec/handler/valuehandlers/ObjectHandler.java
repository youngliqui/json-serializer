package ru.clevertec.handler.valuehandlers;

import ru.clevertec.deserialization.parser.JsonParser;

public class ObjectHandler implements JsonValueHandler {
    private final JsonParser jsonParser;

    public ObjectHandler(JsonParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    @Override
    public Object handle(String value) {
        return jsonParser.parse(value);
    }
}
