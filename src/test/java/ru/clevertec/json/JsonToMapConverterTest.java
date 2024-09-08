package ru.clevertec.json;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.exception.JsonValidationException;
import ru.clevertec.json.parser.JsonParser;
import ru.clevertec.json.validator.JsonValidator;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JsonToMapConverterTest {
    @InjectMocks
    private JsonToMapConverter jsonToMapConverter;
    @Mock
    private JsonValidator jsonValidator;
    @Mock
    private JsonParser jsonParser;

    @Test
    void testConvertJsonToMap() {
        String json = """
                {
                 "name": "Albert"
                }
                """;
        Map<String, Object> jsonMap = Map.of("name", "Albert");

        when(jsonValidator.validate(json)).thenReturn(true);
        when(jsonParser.parse(json)).thenReturn(jsonMap);

        var result = jsonToMapConverter.convertJsonToMap(json);

        verify(jsonValidator, times(1)).validate(json);
        verify(jsonParser, times(1)).parse(json);
        assertEquals(jsonMap, result);
    }

    @Test
    void shouldThrowExceptionWhenJsonIsNotValidate() {
        String json = "dummy";

        when(jsonValidator.validate(json)).thenReturn(false);

        assertThrows(JsonValidationException.class, () ->
                jsonToMapConverter.convertJsonToMap(json));

        verify(jsonValidator, times(1)).validate(json);
        verify(jsonParser, times(0)).parse(json);
    }
}