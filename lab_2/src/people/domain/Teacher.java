package src.people.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Teacher extends Person {
    private final Subject subject;
    private final int workloadHours;

    public Teacher(String fullName, String phone, int birthYear, Subject subject, int workloadHours) {
        super(fullName, phone, birthYear);
        this.subject = subject;
        this.workloadHours = workloadHours;
    }

    @JsonCreator
    public Teacher(
            @JsonProperty("id") String id,
            @JsonProperty("fullName") String fullName,
            @JsonProperty("phone") String phone,
            @JsonProperty("birthYear") int birthYear,
            @JsonProperty("subject") Subject subject,
            @JsonProperty("workloadHours") int workloadHours) {
        super(id, fullName, phone, birthYear);
        this.subject = subject;
        this.workloadHours = workloadHours;
    }

    public Subject getSubject() {
        return subject;
    }
    public int getWorkloadHours() {
        return workloadHours;
    }
}