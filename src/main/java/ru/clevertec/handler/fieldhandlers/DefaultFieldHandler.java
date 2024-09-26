package ru.clevertec.handler.fieldhandlers;

import ru.clevertec.reflection.ReflectionService;

import java.lang.reflect.Field;

public class DefaultFieldHandler implements FieldHandler {
    private final ReflectionService reflectionService;

    public DefaultFieldHandler(ReflectionService reflectionService) {
        this.reflectionService = reflectionService;
    }

    @Override
    public void handleField(Object instance, Field field, Object value) {
        reflectionService.setFieldValue(instance, field, value);
    }
}
