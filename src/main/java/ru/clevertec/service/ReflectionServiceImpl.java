package ru.clevertec.service;

import ru.clevertec.exception.ReflectionsException;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionServiceImpl implements ReflectionService {
    @Override
    public <T> T createInstance(Class<T> tClass) {
        try {
            Constructor<T> constructor = tClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new ReflectionsException("Error creating instance of class: " + tClass.getName() + "; "
                    + e.getMessage());
        }
    }

    @Override
    public void setFieldValue(Object instance, Field field, Object value) {
        try {
            field.set(instance, value);
        } catch (Exception e) {
            throw new ReflectionsException("Error setting filed: " + field.getName() + ", value: " + value + "; "
                    + e.getMessage());
        }
    }

    @Override
    public boolean isCustomObject(Class<?> type) {
        return !type.isPrimitive() && !String.class.equals(type)
                && !type.isArray() && !type.isAssignableFrom(List.class)
                && !Number.class.isAssignableFrom(type);
    }

    @Override
    public <T> void addToList(List<T> list, T element) {
        if (list instanceof ArrayList) {
            list.add(element);
        } else {
            throw new ReflectionsException("Unsupported collection type: " + list.getClass());
        }
    }

    @Override
    public void addToArray(Object array, int index, Object element) {
        if (array.getClass().isArray()) {
            Array.set(array, index, element);
        } else {
            throw new ReflectionsException("Unsupported array type: " + array.getClass());
        }
    }

    @Override
    public Object createCollection(Class<?> collectionType, Class<?> componentType, int size) {
        if (collectionType.isAssignableFrom(ArrayList.class)) {
            return new ArrayList<>(size);
        } else if (collectionType.isArray()) {
            return Array.newInstance(componentType, size);
        } else {
            throw new ReflectionsException("Unsupported collection type: " + collectionType);
        }
    }
}
