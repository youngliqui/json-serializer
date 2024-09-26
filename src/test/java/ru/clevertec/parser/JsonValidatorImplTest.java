package ru.clevertec.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.deserialization.validator.JsonValidator;
import ru.clevertec.deserialization.validator.JsonValidatorImpl;

import static org.assertj.core.api.Assertions.assertThat;

class JsonValidatorImplTest {
    private JsonValidator jsonValidator;

    @BeforeEach
    void setUp() {
        jsonValidator = new JsonValidatorImpl();
    }

    @Test
    void shouldValidateCorrectJson() {
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

        var result = jsonValidator.validate(validJson);

        assertThat(result).isTrue();
    }

    @Test
    void shouldNotValidateIncorrectJson() {
        String incorrectJson = """
                {{
                "name": "Alex",
                "age": 12,
                """;

        var result = jsonValidator.validate(incorrectJson);

        assertThat(result).isFalse();
    }

    @Test
    void shouldNotValidateNullString() {
        var result = jsonValidator.validate(null);

        assertThat(result).isFalse();
    }

    @Test
    void shouldNotValidateEmptyString() {
        String emptyString = "";

        var result = jsonValidator.validate(emptyString);

        assertThat(result).isFalse();
    }
}