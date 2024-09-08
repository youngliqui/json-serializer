package ru.clevertec.json.handler;

public class StringHandler implements JsonValueHandler {
    @Override
    public Object handle(String value) {
        return value.replace("\"", "");
    }
}
