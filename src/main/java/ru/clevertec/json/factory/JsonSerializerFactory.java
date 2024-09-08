package ru.clevertec.json.factory;

import ru.clevertec.json.JsonSerializer;

public interface JsonSerializerFactory {
    JsonSerializer createJsonSerializer();
}
