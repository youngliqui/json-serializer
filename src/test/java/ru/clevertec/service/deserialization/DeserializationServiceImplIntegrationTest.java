package ru.clevertec.service.deserialization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.json.JsonToMapConverter;
import ru.clevertec.json.parser.JsonParserImpl;
import ru.clevertec.json.validator.JsonValidatorImpl;
import ru.clevertec.model.Address;
import ru.clevertec.model.Person;
import ru.clevertec.model.Store;
import ru.clevertec.service.reflection.ReflectionService;
import ru.clevertec.service.reflection.ReflectionServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeserializationServiceImplIntegrationTest {
    private DeserializationService deserializationService;

    @BeforeEach
    void setUp() {
        ReflectionService reflectionService = new ReflectionServiceImpl();
        JsonToMapConverter converter = new JsonToMapConverter(
                new JsonValidatorImpl(), new JsonParserImpl()
        );
        deserializationService = new DeserializationServiceImpl(converter, reflectionService);
    }

    @Test
    void testDeserializeSimpleObject() throws Exception {
        String json = """
                {
                    "name": "Ivan",
                    "age": 10,
                    "isEmployee": true
                }
                """;
        Person expectedPerson = Person.builder()
                .age(10)
                .name("Ivan")
                .isEmployee(true)
                .build();


        Person resultPerson = deserializationService.deserialize(json, Person.class);

        assertThat(resultPerson)
                .isNotNull()
                .isEqualTo(expectedPerson);
    }

    @Test
    void testDeserializeComplexObject() throws Exception {
        String json = """
                {
                    "age": 20,
                    "isEmployee": true,
                    "name": "Ivan",
                    "address": {
                        "street": "Central",
                        "homeNumber": 44,
                        "stores": [
                            {
                                "name": "Supermarket",
                                "category": "Groceries"
                            },
                            {
                                "name": "Electronics Store",
                                "category": "Electronics"
                            }
                        ]
                    },
                    "hobbies": ["Reading", "Traveling", "Cooking"]
                }
                """;

        Store store1 = new Store("Supermarket", "Groceries");
        Store store2 = new Store("Electronics Store", "Electronics");
        Address address = new Address("Central", 44, List.of(store1, store2));

        Person expectedPerson = Person.builder()
                .name("Ivan")
                .isEmployee(true)
                .age(20)
                .hobbies(new String[]{"Reading", "Traveling", "Cooking"})
                .address(address)
                .build();


        Person resultPerson = deserializationService.deserialize(json, Person.class);


        assertThat(resultPerson)
                .isNotNull()
                .isEqualTo(expectedPerson);
    }
}