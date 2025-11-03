package src.people.dao;

import src.people.domain.Person;
import java.util.Optional;

public interface PeopleDao {
    String create(Person p);
    Optional<Person> findById(String id);
    void update(Person p);
    boolean delete(String id);
}