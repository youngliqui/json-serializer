package ru.clevertec.handler;

import ru.clevertec.parser.JsonParser;

public class ObjectHandler implements JsonValueHandler {
    private JsonParser jsonParser;

    public ObjectHandler(JsonParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    @Override
    public Object handle(String value) {
        return jsonParser.parse(value);
    }
}
