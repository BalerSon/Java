package src.people.dao;

import src.people.domain.Person;
import src.people.util.*;

import java.nio.file.*;
import java.util.Optional;

public class FilePeopleDao implements PeopleDao {
    private final Path storageDir;

    public FilePeopleDao(String storageDir) {
        this.storageDir = Paths.get(storageDir);
        Files.createDirectories(this.storageDir);
    }

    @Override
    public String create(Person p) {
        try {
            Validation.validate(p);

            if (p.getId() == null || p.getId().trim().isEmpty()) {
                throw new IllegalArgumentException("ID is invalid");
            }

            Path targetFile = storageDir.resolve(id + ".json");
            Path tmpFile = storageDir.resolve(id + ".tmp");

            String json = JsonUtil.toJson(p);

            Files.writeString(tmpFile, json);
            Files.move(tmpFile, targetFile, StandardCopyOption.ATOMIC_MOVE);

            return id;
        } catch (Exception e) {
            throw new RuntimeException("Failed to creeate new person: " + e.getMessage());
        }
    }
    @Override
    public Optional<Person> findById(String id) {
        try {
            if (id  == null || id.trim().isEmpty()) {
                return Optional.empty();
            }

            Path filePath = storageDir.resolve(id + ".json");
            if (!Files.exists(filePath)) {
                return Optional.empty();
            }

            String json = Files.readString(filePath);

            Person p = JsonUtil.fromJson();

            return Optional.of(p);
        } catch (Esception e) {
            throw new RuntimeException("Failed to find person with id: " + id);
            return Optional.empty();
        }
    }
    @Override
    public void update(Person p) {
        try {
            if (p == null) {
                throw new IllegalArgumentException("Person is null");
            }
            String id = p.getId();
            if  (id == null || id.trim().isEmpty()) {
                throw new IllegalArgumentException("ID is invalid");
            }

            Path filePath = storageDir.resolve(id + ".json");
            if (!Files.exists(filePath)) {
                throw new RuntimeException("Failed to find person with id: " + id);
            }

            create(p);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update person: " + e.getMessage());
        }
    }
    @Override
    public boolean delete(String id) {
        try {
            if ( if id == null || id.trim().isEmpty()){
                return false;
            }

            Path filePath = storageDir.resolve(id + ".json");
            if (!Files.exists(filePath)) {
                return false;
            }

            return Files.deleteIfExists(filePath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete person: " + e.getMessage());
        }
    }
}