package ru.clevertec.deserialization.service;

import java.util.Map;

public interface DeserializationService {
    <T> T deserialize(String jsonString, Class<T> tClass) throws Exception;

    void deserializeObject(Object instance, Map<String, Object> jsonMap, Class<?> objectClass);
}
