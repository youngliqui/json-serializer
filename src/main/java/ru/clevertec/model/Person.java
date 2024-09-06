package ru.clevertec.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Person {
    private int age;
    private boolean isBoy;
    private String name;
    private Address address;
    private int[] scores;
}
