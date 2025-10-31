package src.people.domain;

import java.util.UUID;
import src.people.util.IdGenerator;
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
    private final String id;
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

    @Override
    public boolean equals(Object o) {
        return (o instanceof Person) && ((Person) o).id.equals(this.id);
    }
    @Override
    public boolean hashCode() {
        return Objects.hash(id);
    }
}