package ru.clevertec.deserialization.factory;

import ru.clevertec.deserialization.JsonDeserializer;
import ru.clevertec.deserialization.converter.JsonToMapConverter;
import ru.clevertec.deserialization.parser.JsonParserImpl;
import ru.clevertec.deserialization.validator.JsonValidatorImpl;
import ru.clevertec.deserialization.service.DeserializationService;
import ru.clevertec.deserialization.service.DeserializationServiceImpl;
import ru.clevertec.reflection.ReflectionServiceImpl;

public class JsonDeserializerFactoryImpl implements JsonDeserializerFactory {
    @Override
    public JsonDeserializer createJsonDeserializer() {
        DeserializationService deserializationService = new DeserializationServiceImpl(
                new JsonToMapConverter(
                        new JsonValidatorImpl(), new JsonParserImpl()
                ),
                new ReflectionServiceImpl()
        );
        return new JsonDeserializer(deserializationService);
    }
}
