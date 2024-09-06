package ru.clevertec.handler;

public class IntHandler implements JsonValueHandler {
    @Override
    public Object handle(String value) {
        return Integer.parseInt(value);
    }
}
