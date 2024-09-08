package ru.clevertec.service.deserialization;

public interface DeserializationService {
    <T> T deserialize(String jsonString, Class<T> tClass) throws Exception;
}
