package src.people.domain;

public class Student extends Person {
    private List<Subject> subjects;
    private Map<Subject, Double> averageGrades;

    public Student(String fullName, String phone, int birthYear, List<Subject> subjects, Map<Subject, Double> averageGrades) {
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