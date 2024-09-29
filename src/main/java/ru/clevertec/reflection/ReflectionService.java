package ru.clevertec.reflection;

import java.lang.reflect.Field;
import java.util.List;

public interface ReflectionService {
    <T> T createInstance(Class<T> tClass);

    void setFieldValue(Object instance, Field field, Object value);

    boolean isCustomObject(Class<?> type);

    void addToArray(Object array, int index, Object element);

    <T> void addToList(List<T> collection, T element);

    Object createDataStructure(Class<?> collectionType, Class<?> componentType, int size);
}
