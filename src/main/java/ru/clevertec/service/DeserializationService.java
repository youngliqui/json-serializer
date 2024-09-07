package ru.clevertec.service;

public interface DeserializationService {
    <T> T deserialize(String jsonString, Class<T> tClass) throws Exception;
}
