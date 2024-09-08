package ru.clevertec.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.json.parser.JsonParser;
import ru.clevertec.json.parser.JsonParserImpl;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JsonParserImplTest {
    private JsonParser jsonParser;

    @BeforeEach
    void setUp() {
        jsonParser = new JsonParserImpl();
    }


    @Test
    void shouldParseCorrectJson() {
        String validJson = """
                {
                  "age": 20,
                  "isBoy": true,
                  "name": "Vasya",
                  "address": {
                    "street": "central",
                    "homeNumber": 21
                  },
                  "scores": [5, 4, 2]
                }      
                """;

        Map<String, Object> expectedMap = Map.of(
                "age", 20,
                "isBoy", true,
                "name", "Vasya",
                "address", Map.of(
                        "street", "central",
                        "homeNumber", 21
                ),
                "scores", Arrays.asList(5, 4, 2)
        );

        var actualMap = jsonParser.parse(validJson);

        assertThat(actualMap).hasSize(expectedMap.size());
        assertThat(actualMap).isEqualTo(expectedMap);
    }
}