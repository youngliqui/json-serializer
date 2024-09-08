package ru.clevertec;

import ru.clevertec.json.JsonSerializer;
import ru.clevertec.json.factory.JsonSerializerFactoryImpl;
import ru.clevertec.model.Person;

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

        JsonSerializer jsonSerializer = new JsonSerializerFactoryImpl().createJsonSerializer();
        Person person = jsonSerializer.deserialize(json, Person.class);

        System.out.printf(
                "Person:\nage: %s\nisEmployee: %s\nname: %s\naddress: %s\nhobbies: %s\n",
                person.getAge(), person.isEmployee(), person.getName(), person.getAddress(),
                Arrays.toString(person.getHobbies())
        );
    }
}
