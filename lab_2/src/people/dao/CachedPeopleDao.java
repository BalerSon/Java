package src.people.dao;

import src.people.domain.Person;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class CachedPeopleDao implements PeopleDao {
    private final PeopleDao delegate;
    private final Map<String, Person> cache;

    public CachedPeopleDao(PeopleDao delegate) {
        this.delegate = delegate;
        this.cache = new ConcurrentHashMap<>();
    }

    @Override
    public String create(Person p) {
        String id = delegate.create(p);
        cache.put(id, p);
        return id;
    }
    @Override
    public Optional<Person> findById(String id) {
        if (cache.containsKey(id)) {
            return Optional.of(cache.get(id));
        }

        Optional<Person> person = delegate.findById(id);

        person.ifPresent(p -> cache.put(id, p));

        return person;
    }
    @Override
    public void update(Person p) {
        String id = p.getId();
        delegate.update(p);
        cache.put(id, p);
    }
    @Override
    public boolean delete(String id) {
        boolean deleted = delegate.delete(id);

        if (deleted) {
            cache.remove(id);
        }

        return deleted;
    }

    public void clearCache() {
        cache.clear();
    }

    public int getCacheSize() {
        return cache.size();
    }
}