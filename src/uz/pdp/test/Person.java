package uz.pdp.test;

import java.io.Serializable;

public class Person implements Serializable {
    String name;
    int birthYear;

    public Person() {
    }

    public Person(String name, int birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }
}
