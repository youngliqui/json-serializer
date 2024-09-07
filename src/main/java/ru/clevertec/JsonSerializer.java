package ru.clevertec;

import ru.clevertec.exception.DeserializationException;
import ru.clevertec.service.DeserializationService;

public class JsonSerializer {
    private DeserializationService deserializationService;

    public JsonSerializer(DeserializationService deserializationService) {
        this.deserializationService = deserializationService;
    }

    public <T> T deserialize(String jsonString, Class<T> tClass) {
        try {
            return deserializationService.deserialize(jsonString, tClass);
        } catch (Exception e) {
            throw new DeserializationException("Error with deserialization: " + e.getMessage());
        }
    }
}
