package src.people.domain;

import src.people.util.IdGenerator;

import java.util.UUID;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonTypeInfo (
    use = JsonTypeInfo.Id.NAME,
    property = "type"
)
@JsonSubTypes ({
    @JsonSubTypes.Type(value = Teacher.class, name = "teacher"),
    @JsonSubTypes.Type(value = Student.class, name = "student")
})

public abstract class Person {
    private final String fullName;
    private final String phone;
    private String id;
    private final int birthYear;

    protected Person(String fullName, String phone, int birthYear) {
        this.fullName = fullName;
        this.phone = phone;
        this.id = IdGenerator.generateId();
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

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Person) && ((Person) o).id.equals(this.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}