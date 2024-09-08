package ru.clevertec.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private int age;
    private boolean isEmployee;
    private String name;
    private Address address;
    private String[] hobbies;
}
