package ru.clevertec.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.exception.JsonValidationException;
import ru.clevertec.deserialization.parser.JsonParser;
import ru.clevertec.deserialization.converter.JsonToMapConverter;
import ru.clevertec.deserialization.validator.JsonValidator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JsonToMapConverterTest {
    @InjectMocks
    private JsonToMapConverter jsonToMapConverter;
    @Mock
    private JsonValidator validator;
    @Mock
    private JsonParser jsonParser;

    @Test
    void shouldConvertCorrectJsonToMap() {
        String validJson = """
                 {
                  "age": 20,
                  "isBoy": true,
                  "name": "Vasya",
                  "address": {
                    "street": "central",
                    "homeNumber": 21
                  },
                  "scores": [ 5, 4, 2]
                }         
                """;

        when(validator.validate(validJson)).thenReturn(true);

        jsonToMapConverter.convertJsonToMap(validJson);

        verify(validator, times(1)).validate(validJson);
        verify(jsonParser, times(1)).parse(validJson);
    }

    @Test
    void shouldThrowExceptionWhenConvertIncorrectJsonToMap() {
        String incorrectJson = "dummy";

        when(validator.validate(incorrectJson)).thenReturn(false);

        assertThrows(JsonValidationException.class,
                () -> jsonToMapConverter.convertJsonToMap(incorrectJson));
        verify(validator, times(1)).validate(incorrectJson);
        verify(jsonParser, times(0)).parse(anyString());
    }
}