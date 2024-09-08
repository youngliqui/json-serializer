package ru.clevertec.json;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.exception.DeserializationException;
import ru.clevertec.model.Person;
import ru.clevertec.service.deserialization.DeserializationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JsonSerializerTest {
    @InjectMocks
    private JsonSerializer jsonSerializer;
    @Mock
    private DeserializationService deserializationService;

    @Test
    void testDeserializeJsonString() throws Exception {
        String json = """
                {
                 "name": "JSON"
                }
                """;
        Person person = Person.builder().name("JSON").build();
        when(deserializationService.deserialize(anyString(), any())).thenReturn(person);


        Person resultPerson = jsonSerializer.deserialize(json, Person.class);

        assertEquals(person, resultPerson);
        verify(deserializationService, times(1)).deserialize(json, Person.class);
    }

    @Test
    void testThrowExceptionWhenErrorWithDeserialization() throws Exception {
        String json = "json";
        when(deserializationService.deserialize(anyString(), any()))
                .thenThrow(new RuntimeException("Error with deserialization"));


        assertThrows(DeserializationException.class, () ->
                jsonSerializer.deserialize(json, Person.class));
    }
}