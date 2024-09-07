package ru.clevertec.service;

import ru.clevertec.parser.JsonToMapConverter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

public class DeserializationServiceImpl implements DeserializationService {
    private JsonToMapConverter converter;
    private ReflectionService reflectionService;

    public DeserializationServiceImpl() {
        this.converter = new JsonToMapConverter();
        this.reflectionService = new ReflectionServiceImpl();
    }

    @Override
    public <T> T deserialize(String jsonString, Class<T> tClass) {
        Map<String, Object> jsonMap = converter.convertJsonToMap(jsonString);

        T instance = reflectionService.createInstance(tClass);
        deserializeObject(instance, jsonMap, tClass);

        return instance;
    }

    private void deserializeObject(Object instance, Map<String, Object> jsonMap, Class<?> tClass) {
        for (Field field : tClass.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();

            if (!jsonMap.containsKey(fieldName)) {
                continue;
            }

            Object value = jsonMap.get(fieldName);
            Class<?> fieldType = field.getType();

            if (fieldType.isArray()) {
                handleArrayField(instance, field, value);
            } else if (List.class.isAssignableFrom(fieldType)) {
                handleListField(instance, field, value);
            } else if (reflectionService.isCustomObject(fieldType)) {
                handleNestedObject(instance, field, value);
            } else {
                reflectionService.setFieldValue(instance, field, value);
            }

            field.setAccessible(false);
        }
    }

    private <T> void handleArrayField(T instance, Field field, Object value) {
        Class<?> componentType = field.getType().getComponentType();
        int size = ((List<?>) value).size();
        Object array = reflectionService.createCollection(field.getType(), componentType, size);

        for (int index = 0; index < size; index++) {
            Object item = ((List<?>) value).get(index);
            if (item instanceof Map) {
                Object nestedObject = reflectionService.createInstance(componentType);
                deserializeObject(nestedObject, (Map<String, Object>) item, componentType);
                reflectionService.addToArray(array, index, nestedObject);
            } else {
                reflectionService.addToArray(array, index, item);
            }
        }
        reflectionService.setFieldValue(instance, field, array);
    }

    private <T> void handleListField(T instance, Field field, Object value) {
        Class<?> componentType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        List<?> valueList = (List<?>) value;
        Object list = reflectionService.createCollection(field.getType(), componentType, valueList.size());

        if (reflectionService.isCustomObject(componentType)) {
            valueList.stream()
                    .filter(item -> item instanceof Map)
                    .forEach(item -> {
                        Object nestedObject = reflectionService.createInstance(componentType);
                        deserializeObject(nestedObject, (Map<String, Object>) item, componentType);
                        reflectionService.addToList((List<T>) list, (T) nestedObject);
                    });
            reflectionService.setFieldValue(instance, field, list);
        } else {
            reflectionService.setFieldValue(instance, field, valueList);
        }
    }

    private <T> void handleNestedObject(T instance, Field field, Object value) {
        Object nestedObject = reflectionService.createInstance(field.getType());
        deserializeObject((T) nestedObject, (Map<String, Object>) value, (Class<T>) field.getType());
        reflectionService.setFieldValue(instance, field, nestedObject);
    }
}
