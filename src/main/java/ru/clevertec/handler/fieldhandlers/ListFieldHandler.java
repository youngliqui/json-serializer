package ru.clevertec.handler.fieldhandlers;

import ru.clevertec.deserialization.service.DeserializationService;
import ru.clevertec.reflection.ReflectionService;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

public class ListFieldHandler implements FieldHandler {
    private final ReflectionService reflectionService;
    private final DeserializationService deserializationService;

    public ListFieldHandler(ReflectionService reflectionService, DeserializationService deserializationService) {
        this.reflectionService = reflectionService;
        this.deserializationService = deserializationService;
    }

    @Override
    public void handleField(Object instance, Field field, Object value) {
        Class<?> componentType = getComponentType(field);
        List<?> valueList = (List<?>) value;
        Object list = reflectionService.createDataStructure(field.getType(), componentType, valueList.size());

        if (reflectionService.isCustomObject(componentType)) {
            populateCustomObjectList(valueList, componentType, list);
            reflectionService.setFieldValue(instance, field, list);
        } else {
            reflectionService.setFieldValue(instance, field, valueList);
        }
    }

    private Class<?> getComponentType(Field field) {
        return (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
    }

    private <T> void populateCustomObjectList(List<?> valueList, Class<?> componentType, Object list) {
        valueList.stream()
                .filter(item -> item instanceof Map)
                .forEach(item -> {
                    Object nestedObject = reflectionService.createInstance(componentType);
                    deserializationService.deserializeObject(nestedObject, (Map<String, Object>) item, componentType);
                    reflectionService.addToList((List<T>) list, (T) nestedObject);
                });
    }
}
