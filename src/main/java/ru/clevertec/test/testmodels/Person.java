package ru.clevertec.test.testmodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {
    private int age;
    private boolean isEmployee;
    private String name;
    private Address address;
    private String[] hobbies;
}
