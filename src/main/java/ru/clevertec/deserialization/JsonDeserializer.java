package ru.clevertec.deserialization;

import ru.clevertec.exception.DeserializationException;
import ru.clevertec.deserialization.service.DeserializationService;

public class JsonDeserializer {
    private final DeserializationService deserializationService;

    public JsonDeserializer(DeserializationService deserializationService) {
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
