package ru.clevertec.service.reflection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.exception.ReflectionsException;
import ru.clevertec.reflection.ReflectionServiceImpl;
import ru.clevertec.test.testmodels.Person;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReflectionServiceImplTest {
    private ReflectionServiceImpl reflectionService;

    @BeforeEach
    void setUp() {
        reflectionService = new ReflectionServiceImpl();
    }

    @Test
    void testCreateInstance() {
        Person expectedPerson = new Person();

        var resultInstance = reflectionService.createInstance(Person.class);

        assertThat(resultInstance)
                .isNotNull()
                .isEqualTo(expectedPerson);
    }

    @Test
    void shouldSetFieldValue() throws NoSuchFieldException {
        Person person = new Person();
        Field field = Person.class.getDeclaredField("name");
        field.setAccessible(true);

        reflectionService.setFieldValue(person, field, "PersonName");

        field.setAccessible(false);
        assertThat(person.getName())
                .isNotNull()
                .isEqualTo("PersonName");
    }

    @Test
    void testIsCustomObject() {
        assertThat(reflectionService.isCustomObject(Person.class)).isTrue();
        assertThat(reflectionService.isCustomObject(String.class)).isFalse();
        assertThat(reflectionService.isCustomObject(Integer.class)).isFalse();
        assertThat(reflectionService.isCustomObject(int.class)).isFalse();
        assertThat(reflectionService.isCustomObject(boolean.class)).isFalse();
        assertThat(reflectionService.isCustomObject(List.class)).isFalse();
    }

    @Test
    void testAddToList() {
        List<String> list = new ArrayList<>();

        reflectionService.addToList(list, "item1");
        reflectionService.addToList(list, "item2");

        assertThat(list)
                .hasSize(2)
                .contains("item1", "item2");
    }

    @Test
    void testAddToArray() {
        String[] array = new String[2];

        reflectionService.addToArray(array, 0, "item1");
        reflectionService.addToArray(array, 1, "item2");

        assertThat(array)
                .contains("item1", "item2");
    }

    @Test
    void testCreateCollection() {
        List<String> list = (List<String>) reflectionService.createDataStructure(ArrayList.class, String.class, 2);
        String[] array = (String[]) reflectionService.createDataStructure(String[].class, String.class, 2);

        assertThat(list).hasSize(0);
        assertThat(array).hasSize(2);
    }

    @Test
    void testCreateCollectionUnsupportedType() {
        assertThrows(ReflectionsException.class,
                () -> reflectionService.createDataStructure(Integer.class, String.class, 2));
    }

    @Test
    void testAddToArrayUnsupportedType() {
        assertThrows(ReflectionsException.class,
                () -> reflectionService.addToArray(new Object(), 0, "item"));
    }

    @Test
    void testCreateInstanceThrowException() {
        class TestClass {
            private String name;

            public TestClass() throws Exception {
                throw new Exception("Constructor error");
            }
        }

        assertThrows(ReflectionsException.class, () ->
                reflectionService.createInstance(TestClass.class)
        );
    }

    @Test
    void testSetFieldValueThrowsException() throws NoSuchFieldException {
        Person person = new Person();
        Field field = Person.class.getDeclaredField("name");

        assertThrows(ReflectionsException.class, () ->
                reflectionService.setFieldValue(person, field, "personName"));
    }
}