package ru.clevertec.handler.fieldhandlers;

import java.lang.reflect.Field;

public interface FieldHandler {
    void handleField(Object instance, Field field, Object value);
}
