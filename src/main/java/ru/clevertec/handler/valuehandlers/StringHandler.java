package ru.clevertec.handler.valuehandlers;

public class StringHandler implements JsonValueHandler {
    @Override
    public Object handle(String value) {
        return value.replace("\"", "");
    }
}
