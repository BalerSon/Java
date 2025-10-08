package src.people.domain;

import java.util.Map;
import java.util.List;
import java.util.LinkedHashSet;

public class Student extends Person {
    private final List<Subject> subjects;
    private final Map<Subject, Double> averageGrades;

    public Student(String fullName, String phone, int birthYear, List<Subject> subjects, Map<Subject, Double> averageGrades) {
        for (Double grade : averageGrades.values()) {
            if (grade < 0 || grade > 10) {
                throw new IllegalArgumentException("Grade must be between 0 and 10");
            }
        }

        super(fullName, phone, birthYear);
        this.subjects = List.copyOf(new LinkedHashSet<>(subjects));
        this.averageGrades = List.copyOf(averageGrades);
    }

    public List<Subject> getSubjects() {
        return subjects;
    }
    public Map<Subject, Double> getAverageGrades() {
        return averageGrades;
    }
}