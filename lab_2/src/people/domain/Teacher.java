package src.people.domain;

public class Teacher extends Person {
    private final Subject subject;
    private final int workloadHours;

    public Teacher(String fullName, String phone, int birthYear, Subject subject, int workloadHours) {
        super(fullName, phone, birthYear);
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