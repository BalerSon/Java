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
                throw new IllegalArgumentException("ID is required");
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

    }
    @Override
    public void update(Person p) {

    }
    @Override
    public boolean delete(String id) {

    }
}