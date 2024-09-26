package ru.clevertec.handler.fieldhandlers;

import ru.clevertec.deserialization.service.DeserializationService;
import ru.clevertec.reflection.ReflectionService;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class ArrayFieldHandler implements FieldHandler {
    private final ReflectionService reflectionService;
    private final DeserializationService deserializationService;

    public ArrayFieldHandler(ReflectionService reflectionService, DeserializationService deserializationService) {
        this.reflectionService = reflectionService;
        this.deserializationService = deserializationService;
    }

    @Override
    public void handleField(Object instance, Field field, Object value) {
        Class<?> componentType = field.getType().getComponentType();
        int size = ((List<?>) value).size();
        Object array = reflectionService.createDataStructure(field.getType(), componentType, size);

        for (int index = 0; index < size; index++) {
            Object item = ((List<?>) value).get(index);
            if (item instanceof Map) {
                Object nestedObject = deserializeNestedObject(item, componentType);
                reflectionService.addToArray(array, index, nestedObject);
            } else {
                reflectionService.addToArray(array, index, item);
            }
        }
        reflectionService.setFieldValue(instance, field, array);
    }

    private Object deserializeNestedObject(Object item, Class<?> componentType) {
        Object nestedObject = reflectionService.createInstance(componentType);
        deserializationService.deserializeObject(nestedObject, (Map<String, Object>) item, componentType);

        return nestedObject;
    }
}
