package src.people.util;

import src.people.domain.Person;
import src.people.domain.Student;
import src.people.domain.Teacher;
import src.people.domain.Subject;

import java.util.List;
import java.util.Map;

public class Validation {
    public static void validatePerson(Person person) {
        if (person == null) {
            throw new NullPointerException("Person is null");
        }
        validateFullName(person.getFullName());
        validatePhone(person.getPhone());
        validateBirthYear(person.getBirthYear());
    }

    public static void validateTeacher(Teacher teacher) {
        validatePerson(teacher);
        int workloadHours = teacher.getWorkloadHours();

        if (workloadHours < 0) {
            throw new IllegalArgumentException("Workload hours cannot be negative");
        }
        if (teacher.getSubject() == null) {
            throw new IllegalArgumentException("Subject cannot be null");
        }
    }
    public static void validateStudent(Student student) {
        validatePerson(student);

        List<Subject> subjects = student.getSubjects();
        if (subjects == null || subjects.isEmpty()) {
            throw new IllegalArgumentException("It should at least one subject");
        }

        Map<Subject, Double> grades = student.getAverageGrades();
        if (grades == null) {
            throw new IllegalArgumentException("Grades map cannot be null");
        }
        for (Double grade : grades.values()) {
            if (grade < 0 || grade > 10) {
                throw new IllegalArgumentException("Grade must be between 0 and 10");
            }
        }
    }

    public static void validateFullName(String fullName) {
        fullName = fullName.trim();
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Incorrect fullName! It can't be blank!");
        }
    }
    public static void validatePhone(String phone) {
        phone = phone.trim();
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Incorrect phone! It can't be blank!");
        }
    }
    public static void validateBirthYear(int birthYear) {
        if (birthYear < 1900 || birthYear > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("Incorrect birthDate, elder!");
        }
    }
}