package src.people.control;

import src.people.service.PeopleService;
import src.people.domain.Subject;

import java.util.Map;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Dispatcher implements Runnable {
    private final BlockingQueue<Command> commandQueue;
    private final PeopleService peopleService;
    private volatile boolean running = true;

    public Dispatcher(BlockingQueue<Command> commandQueue, PeopleService peopleService) {
        this.commandQueue = commandQueue;
        this.peopleService = peopleService;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Command command = commandQueue.take();
                processCommand(command);
            } catch (InterruptedException e) {
                System.out.println("Dispatcher interrupted");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("Dispatcher exception " + e.getMessage());
            }
        }
    }

    private void processCommand(Command command) {
        try {
            System.out.println("Command processing " + command.getOp());

            switch (command.getOp()) {
                case "CreateStudent":
                    handleCreateStudent(command.getPayload());
                    break;
                case "CreateTeacher":
                    handleCreateTeacher(command.getPayload());
                    break;
                case "UpdateStudent":
                    handleUpdateStudent(command.getPayload());
                    break;
                case "UpdateTeacher":
                    handleUpdateTeacher(command.getPayload());
                    break;
                case "Delete":
                    handleDelete(command.getPayload());
                    break;
                case "Get":
                    handleGet(command.getPayload());
                    break;
                default:
                    System.err.println("Unknown operation " + command.getOp());
            }
            System.out.println("Command" + command.getOp() + " executed");
        } catch (Exception e) {
            System.err.println("Command " + command.getOp() + " failed: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void handleCreateStudent(Map<String, Object> payload) {
        String fullName = (String) payload.get("fullName");
        Integer birthYear = ((Number) payload.get("birthYear")).intValue();
        String phone = (String) payload.get("phone");
        List<String> subjectStrs = (List<String>) payload.get("studiedSubjects");
        Map<String, Double> gradeStrs = (Map<String, Double>) payload.get("averageGrades");

        List<Subject> studiedSubjects = subjectStrs.stream().map(Subject::valueOf).toList();

        Map<Subject, Double> averageGrades = gradeStrs.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(e -> Subject.valueOf(e.getKey()), Map.Entry::getValue));

        String id = peopleService.createStudent(fullName, birthYear, phone, studiedSubjects, averageGrades);
        System.out.println("Create student with id: " + id);
    }

    private void handleCreateTeacher(Map<String, Object> payload) {
        String fullName = (String) payload.get("fullName");
        Integer birthYear = ((Number) payload.get("birthYear")).intValue();
        String phone = (String) payload.get("phone");
        Subject subject = Subject.valueOf((String) payload.get("subject"));
        Integer workloadHours = ((Number) payload.get("workloadHours")).intValue();

        String id = peopleService.createTeacher(fullName, birthYear, phone, subject, workloadHours);
        System.out.println("Create teacher with id: " + id);
    }

    @SuppressWarnings("unchecked")
    private void handleUpdateStudent(Map<String, Object> payload) {
        String id = (String) payload.get("id");

        String fullName = (String) payload.get("fullName");
        Integer birthYear = payload.get("birthYear") != null ? ((Number) payload.get("birthYear")).intValue() : null;
        String phone = (String) payload.get("phone");

        List<Subject> studiedSubjects = null;
        if (payload.get("studiedSubjects") != null) {
            List<String> subjectStrs = (List<String>) payload.get("studiedSubjects");
            studiedSubjects = subjectStrs.stream().map(Subject::valueOf).toList();
        }

        Map<Subject, Double> averageGrades = null;
        if (payload.get("averageGrades") != null) {
            Map<String, Double> gradeStrs = (Map<String, Double>) payload.get("averageGrades");
            averageGrades = gradeStrs.entrySet().stream()
                    .collect(java.util.stream.Collectors.toMap(e -> Subject.valueOf(e.getKey()), Map.Entry::getValue));
        }

        peopleService.updateStudent(id, fullName, birthYear, phone, studiedSubjects, averageGrades);
        System.out.println("Update student with id: " + id);
    }

    private void handleUpdateTeacher(Map<String, Object> payload) {
        String id = (String) payload.get("id");

        String fullName = (String) payload.get("fullName");
        Integer birthYear = payload.get("birthYear") != null ?
                ((Number) payload.get("birthYear")).intValue() : null;
        String phone = (String) payload.get("phone");
        Subject subject = payload.get("subject") != null ?
                Subject.valueOf((String) payload.get("subject")) : null;
        Integer workloadHours = payload.get("workloadHours") != null ?
                ((Number) payload.get("workloadHours")).intValue() : null;

        peopleService.updateTeacher(id, fullName, birthYear, phone, subject, workloadHours);
        System.out.println("Update teacher with id: " + id);
    }

    private void handleDelete(Map<String, Object> payload) {
        String id = (String) payload.get("id");
        boolean deleted = peopleService.deleteById(id);
        if (deleted) {
            System.out.println("Deleted student with id: " + id);
        } else {
            System.out.println("Failed to delete student with id: " + id);
        }
    }

    private void handleGet(Map<String, Object> payload) {
        String id = (String) payload.get("id");
        var person = peopleService.getById(id);
        if (person.isPresent()) {
            System.out.println("Found person with id: " + id);
        } else {
            System.out.println("Failed to find person with id: " + id);
        }
    }

    public void stop() {
        running = false;
    }
}