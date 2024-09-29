package ru.clevertec.handler.valuehandlers;

public class BooleanHandler implements JsonValueHandler {
    @Override
    public Object handle(String value) {
        return Boolean.parseBoolean(value);
    }
}
