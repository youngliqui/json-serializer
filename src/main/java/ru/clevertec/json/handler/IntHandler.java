package ru.clevertec.json.handler;

public class IntHandler implements JsonValueHandler {
    @Override
    public Object handle(String value) {
        return Integer.parseInt(value);
    }
}
