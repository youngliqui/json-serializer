package ru.clevertec.test.testmodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.annotation.JsonField;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String street;
    private int homeNumber;
    @JsonField("stores")
    private List<Store> nearbyStores;
}
