package ru.clevertec.json.factory;

import ru.clevertec.json.JsonSerializer;
import ru.clevertec.json.JsonToMapConverter;
import ru.clevertec.json.parser.JsonParserImpl;
import ru.clevertec.json.validator.JsonValidatorImpl;
import ru.clevertec.service.deserialization.DeserializationService;
import ru.clevertec.service.deserialization.DeserializationServiceImpl;
import ru.clevertec.service.reflection.ReflectionServiceImpl;

public class JsonSerializerFactoryImpl implements JsonSerializerFactory {
    @Override
    public JsonSerializer createJsonSerializer() {
        DeserializationService deserializationService = new DeserializationServiceImpl(
                new JsonToMapConverter(
                        new JsonValidatorImpl(), new JsonParserImpl()
                ),
                new ReflectionServiceImpl()
        );
        return new JsonSerializer(deserializationService);
    }
}
