package ru.clevertec.deserialization.factory;

import ru.clevertec.deserialization.JsonDeserializer;

public interface JsonDeserializerFactory {
    JsonDeserializer createJsonDeserializer();
}
