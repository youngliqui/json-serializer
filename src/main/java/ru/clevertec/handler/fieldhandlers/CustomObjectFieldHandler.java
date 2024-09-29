package ru.clevertec.handler.fieldhandlers;

import ru.clevertec.deserialization.service.DeserializationService;
import ru.clevertec.reflection.ReflectionService;

import java.lang.reflect.Field;
import java.util.Map;

public class CustomObjectFieldHandler implements FieldHandler {
    private final ReflectionService reflectionService;
    private final DeserializationService deserializationService;

    public CustomObjectFieldHandler(ReflectionService reflectionService, DeserializationService deserializationService) {
        this.reflectionService = reflectionService;
        this.deserializationService = deserializationService;
    }

    @Override
    public void handleField(Object instance, Field field, Object value) {
        Object nestedObject = reflectionService.createInstance(field.getType());
        deserializationService.deserializeObject(nestedObject, (Map<String, Object>) value, field.getType());
        reflectionService.setFieldValue(instance, field, nestedObject);
    }
}
