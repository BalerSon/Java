package src.people.domain;

public class Teacher extends Person {
    private Subject subject;
    private int workloadHours;

    public Teacher(String fullName, String phone, int birthYear, Subject subject, int workloadHours) {
        if (workloadHours < 0) {
            throw new IllegalArgumentException("Workload hours cannot be negative");
        }

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