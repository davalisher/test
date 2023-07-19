package uz.pdp.test;

import java.io.Serializable;

public class Student extends Person implements Serializable {
    String university;

    public Student() {
    }

    public Student(String university) {
        this.university = university;
    }

    public Student(String name, int birthYear, String university) {
        super(name, birthYear);
        this.university = university;
    }

    @Override
    public String toString() {
        return "Student{" +
                "university='" + university + '\'' +
                ", name='" + name + '\'' +
                ", birthYear=" + birthYear +
                '}';
    }
}
