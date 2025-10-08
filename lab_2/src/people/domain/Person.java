package src.people.domain;

import java.util.UUID;

public abstract class Person {
    private final String fullName;
    private final String phone;
    private final String id;
    private final int birthYear;

    protected Person(String fullName, String phone, int birthYear) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentsException("Incorrect fullName! It can't be blank!");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentsException("Incorrect phone! It can't be blank!");
        }
        if (birthYear < 1900 || birthYear > java.time.Year.now().getValue()) {
            throw new IllegalArgumentsException("Incorrect birthDate, elder!");
        }

        this.fullName = fullName;
        this.phone = phone;
        this.id = UUID.randomUUID().toString();
        this.birthYear = birthYear;
    }

    public String getFullName() {
        return fullName;
    }
    public String getPhone() {
        return phone;
    }
    public String getId() {
        return id;
    }
    public int getBirthYear() {
        return birthYear;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Person) && ((Person) o).id.equals(this.id);
    }
    @Override
    public boolean hashCode() {
        return Objects.hash(id);
    }
}