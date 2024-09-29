package ru.clevertec.reflection;

import ru.clevertec.exception.ReflectionsException;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class ReflectionServiceImpl implements ReflectionService {
    private final Map<Predicate<Class<?>>, BiFunction<Class<?>, Integer, Object>> dataStructureTypeHandlers;
    private final List<Predicate<Class<?>>> customObjectPredicates;

    public ReflectionServiceImpl() {
        dataStructureTypeHandlers = Map.of(
                type -> type.isAssignableFrom(ArrayList.class), (componentType, size) -> new ArrayList<>(size),
                Class::isArray, Array::newInstance
        );
        customObjectPredicates = List.of(
                (type) -> !type.isPrimitive(),
                (type) -> !String.class.equals(type),
                (type) -> !type.isArray(),
                (type) -> !type.isAssignableFrom(List.class),
                (type) -> !Number.class.isAssignableFrom(type)
        );
    }

    @Override
    public <T> T createInstance(Class<T> tClass) {
        Constructor<T> constructor = null;
        try {
            constructor = tClass.getDeclaredConstructor();
            constructor.setAccessible(true);

            return constructor.newInstance();
        } catch (Exception e) {
            throw new ReflectionsException("Error creating instance of class: " + tClass.getName() + "; "
                    + e.getMessage());
        } finally {
            if (constructor != null) {
                constructor.setAccessible(false);
            }
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
        return customObjectPredicates.stream()
                .allMatch((val) -> val.test(type));
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
    public Object createDataStructure(Class<?> dataStructureType, Class<?> componentType, int size) {
        return dataStructureTypeHandlers.entrySet()
                .stream()
                .filter(entry -> entry.getKey().test(dataStructureType))
                .findFirst()
                .map(Map.Entry::getValue)
                .map(val -> val.apply(componentType, size))
                .orElseThrow(() ->
                        new ReflectionsException("Unsupported collection type: " + dataStructureType));
    }
}
