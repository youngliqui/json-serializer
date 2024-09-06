package ru.clevertec.exception;

public class JsonValidationException extends RuntimeException {
    public JsonValidationException() {
        super("The json string is not valid");
    }
}
