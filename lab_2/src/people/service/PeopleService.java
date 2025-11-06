package src.people.service;

import src.people.domain.*;
import src.people.dao.PeopleDao;
import src.people.util.Validation;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PeopleService {
    private final PeopleDao peopleDao;

    public PeopleService(PeopleDao peopleDao) {
        this.peopleDao = peopleDao;
    }

    public String createTeacher(String fullName, int birthYear, String phone, Subject subject, int workloadHours) {
        Teacher teacher = new Teacher(fullName, phone, birthYear, subject, workloadHours);
        Validation.validateTeacher(teacher);
        return peopleDao.create(teacher);
    }

    public String createStudent(String fullName, int birthYear, String phone, List<Subject> studiedSubjects, Map<Subject, Double> averageGrades) {
        Student student = new Student(fullName, phone, birthYear, studiedSubjects, averageGrades);
        Validation.validateStudent(student);
        return peopleDao.create(student);
    }

    public void updateTeacher(String id, String fullName, Integer birthYear, String phone,
                              Subject subject, Integer workloadHours) {
        Optional<Person> teacher = peopleDao.findById(id);

        if (teacher.isEmpty()) {
            throw new IllegalArgumentException("No teacher with id " + id);
        }
        if (!(teacher.get() instanceof Teacher)) {
            throw new IllegalArgumentException("Person with id " + id + " is not a teacher");
        }

        Teacher currentTeacher = (Teacher) teacher.get();

        String newFullName = fullName != null ? fullName : currentTeacher.getFullName();
        int newBirthYear = birthYear != null ? birthYear : currentTeacher.getBirthYear();
        String newPhone = phone != null ? phone : currentTeacher.getPhone();
        Subject newSubject = subject != null ? subject : currentTeacher.getSubject();
        int newWorkloadHours = workloadHours != null ? workloadHours : currentTeacher.getWorkloadHours();

        Teacher updatedTeacher = new Teacher(newFullName, newPhone, newBirthYear, newSubject, newWorkloadHours);

        if (newWorkloadHours < 0) {
            throw new IllegalArgumentException("Workload Hours cannot be negative");
        }

        updatedTeacher.setId(id);

        Validation.validateTeacher(updatedTeacher);
        peopleDao.update(updatedTeacher);
    }

    public void updateStudent(String id, String fullName, Integer birthYear, String phone,
                              List<Subject> studiedSubjects, Map<Subject, Double> averageGrades) {
        Optional<Person> student = peopleDao.findById(id);

        if (student.isEmpty()) {
            throw new IllegalArgumentException("No student with id " + id);
        }
        if (!(student.get() instanceof Student)) {
            throw new IllegalArgumentException("Person with id " + id + " is not a student");
        }

        Student currentStudent = (Student) student.get();

        String newFullName = fullName != null ? fullName : currentStudent.getFullName();
        int newBirthYear = birthYear != null ? birthYear : currentStudent.getBirthYear();
        String newPhone = phone != null ? phone : currentStudent.getPhone();
        List<Subject> newSubjects = studiedSubjects != null ? studiedSubjects : currentStudent.getSubjects();
        Map<Subject, Double> newGrades = averageGrades != null ? averageGrades : currentStudent.getAverageGrades();

        if (newSubjects.isEmpty()) {
            throw new IllegalArgumentException("Ученик должен изучать хотя бы один предмет");
        }

        Student updatedStudent = new Student(newFullName, newPhone, newBirthYear, newSubjects, newGrades);
        updatedStudent.setId(id);

        Validation.validateStudent(updatedStudent);
        peopleDao.update(updatedStudent);
    }

    public Optional getById(String id) {
        return peopleDao.findById(id);
    }

    public boolean deleteById(String id) {
        return peopleDao.delete(id);
    }
}