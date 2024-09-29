package ru.clevertec.test;

import ru.clevertec.deserialization.JsonDeserializer;
import ru.clevertec.deserialization.factory.JsonDeserializerFactoryImpl;
import ru.clevertec.test.testmodels.Person;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
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

        JsonDeserializer jsonDeserializer = new JsonDeserializerFactoryImpl().createJsonDeserializer();
        Person person = jsonDeserializer.deserialize(json, Person.class);

        System.out.printf(
                "Person:\nage: %s\nisEmployee: %s\nname: %s\naddress: %s\nhobbies: %s\n",
                person.getAge(), person.isEmployee(), person.getName(), person.getAddress(),
                Arrays.toString(person.getHobbies())
        );
    }
}
