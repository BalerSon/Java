package src.people.domain;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Student extends Person {
    private final List<Subject> subjects;
    private final Map<Subject, Double> averageGrades;

    public Student(String fullName, String phone, int birthYear, List<Subject> subjects, Map<Subject, Double> averageGrades) {
        super(fullName, phone, birthYear);
        this.subjects = subjects != null ?
                new ArrayList<>(subjects) : new ArrayList<>();
        this.averageGrades = averageGrades != null ?
                new HashMap<>(averageGrades) : new HashMap<>();
    }

    @JsonCreator
    public Student(
            @JsonProperty("id") String id,
            @JsonProperty("fullName") String fullName,
            @JsonProperty("phone") String phone,
            @JsonProperty("birthYear") int birthYear,
            @JsonProperty("subjects") List<Subject> subjects,
            @JsonProperty("averageGrades") Map<Subject, Double> averageGrades) {
        super(id, fullName, phone, birthYear);
        this.subjects = subjects != null ? new ArrayList<>(subjects) : new ArrayList<>();
        this.averageGrades = averageGrades != null ? new HashMap<>(averageGrades) : new HashMap<>();
    }

    public List<Subject> getSubjects() {
        return subjects;
    }
    public Map<Subject, Double> getAverageGrades() {
        return averageGrades;
    }
}