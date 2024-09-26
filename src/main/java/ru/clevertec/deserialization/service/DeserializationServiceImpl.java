package ru.clevertec.deserialization.service;

import ru.clevertec.annotation.JsonField;
import ru.clevertec.deserialization.converter.JsonToMapConverter;
import ru.clevertec.handler.fieldhandlers.*;
import ru.clevertec.reflection.ReflectionService;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class DeserializationServiceImpl implements DeserializationService {
    private final JsonToMapConverter converter;
    private final ReflectionService reflectionService;

    private final Map<Predicate<Class<?>>, FieldHandler> fieldHandlers;

    public DeserializationServiceImpl(JsonToMapConverter converter, ReflectionService reflectionService) {
        this.converter = converter;
        this.reflectionService = reflectionService;
        fieldHandlers = Map.of(
                Class::isArray, new ArrayFieldHandler(reflectionService, this),
                List.class::isAssignableFrom, new ListFieldHandler(reflectionService, this),
                reflectionService::isCustomObject, new CustomObjectFieldHandler(reflectionService, this)
        );
    }

    @Override
    public <T> T deserialize(String jsonString, Class<T> tClass) {
        Map<String, Object> jsonMap = converter.convertJsonToMap(jsonString);

        T instance = reflectionService.createInstance(tClass);
        deserializeObject(instance, jsonMap, tClass);

        return instance;
    }

    @Override
    public void deserializeObject(Object instance, Map<String, Object> jsonMap, Class<?> objectClass) {
        for (Field field : objectClass.getDeclaredFields()) {
            field.setAccessible(true);

            String fieldName = field.isAnnotationPresent(JsonField.class) ?
                    field.getAnnotation(JsonField.class).value() : field.getName();

            if (!jsonMap.containsKey(fieldName)) {
                continue;
            }

            Object value = jsonMap.get(fieldName);
            Class<?> fieldType = field.getType();

            fieldHandlers.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().test(fieldType))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .ifPresentOrElse(val -> val.handleField(instance, field, value), () ->
                            new DefaultFieldHandler(reflectionService).handleField(instance, field, value));

            field.setAccessible(false);
        }
    }
}
