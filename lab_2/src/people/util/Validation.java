package src.people.util;

import src.people.domain.Person;
import src.people.domain.Student;
import src.people.domain.Teacher;
import src.people.domain.Subject;

public class Validation {
    public static void validatePerson(Person person) {
        if (Person == null) {
            throw new NullPointerException("Person is null");
        }
        validateFullName(person.getFullName());
        validatePhone(person.getPhone());
        validateBirthYear(person.getBirthYear());
    }

    public static void validateTeacher(Teacher teacher) {
        validatePerson(teacher);

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
        if (subjects == null || subjects.isEmpty) {
            throw new IllegalArgumentException("It should at least one subject");
        }

        Map<Subject, Double> grades = student.getAverageGrades();
        if (grades == null) {
            throw new IllegalArgumentException("Grades map cannot be null");
        }
        for (Double grade : averageGrades.values()) {
            if (grade < 0 || grade > 10) {
                throw new IllegalArgumentException("Grade must be between 0 and 10");
            }
        }
    }

    public static void validateFullName(String fullName) {
        fullName = fullName.trim();
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentsException("Incorrect fullName! It can't be blank!");
        }
    }
    public static void validatePhone(String phone) {
        phone = phone.trim();
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentsException("Incorrect phone! It can't be blank!");
        }
    }
    public static void validateBirthYear(int birthYear) {
        if (birthYear < 1900 || birthYear > java.time.Year.now().getValue()) {
            throw new IllegalArgumentsException("Incorrect birthDate, elder!");
        }
    }
}