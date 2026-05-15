package be.ucll.unit.repository;

import be.ucll.model.User;
import be.ucll.repository.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class UserRepositoryStub implements UserRepository {

    public List<User> users;

    public UserRepositoryStub() {
        resetRepositoryData();
    }

    public void resetRepositoryData() {
        users = new ArrayList<>(List.of(
                new User("John Doe", 25, "john.doe@ucll.be", "john1234"),
                new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234"),
                new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234"),
                new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234"),
                new User("Birgit Doe", 18, "birgit.doe@ucll.be", "birgit1234")
        ));
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = new ArrayList<>(users);
    }

    public boolean userExists(String email) {
        return existsByEmail(email);
    }

    @Override
    public List<User> findByAgeGreaterThan(int age) {
        List<User> result = new ArrayList<>();

        for (User user : users) {
            if (user.getAge() > age) {
                result.add(user);
            }
        }

        return result;
    }

    @Override
    public List<User> findByAgeBetween(int minAge, int maxAge) {
        List<User> result = new ArrayList<>();

        for (User user : users) {
            if (user.getAge() >= minAge && user.getAge() <= maxAge) {
                result.add(user);
            }
        }

        return result;
    }

    @Override
    public List<User> findByName(String name) {
        List<User> result = new ArrayList<>();

        for (User user : users) {
            if (user.getName().contains(name)) {
                result.add(user);
            }
        }

        return result;
    }

    @Override
    public boolean existsByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<User> findAllByOrderByAgeDesc() {
        List<User> result = new ArrayList<>(users);

        for (int i = 0; i < result.size(); i++) {
            for (int j = i + 1; j < result.size(); j++) {
                if (result.get(i).getAge() < result.get(j).getAge()) {
                    User temporaryUser = result.get(i);
                    result.set(i, result.get(j));
                    result.set(j, temporaryUser);
                }
            }
        }

        return result;
    }

    @Override
    public List<User> findByNameContainingAndAgeGreaterThan(String chars, int age) {
        List<User> result = new ArrayList<>();

        for (User user : users) {
            if (user.getName().contains(chars) && user.getAge() > age) {
                result.add(user);
            }
        }

        return result;
    }

    @Override
    public List<User> findByProfileInterestsContainingIgnoreCase(String interest) {
        List<User> result = new ArrayList<>();

        for (User user : users) {
            if (user.getProfile() != null && user.getProfile().getInterests().toLowerCase().contains(interest.toLowerCase())) {
                result.add(user);
            }
        }

        return result;
    }

    @Override
    public List<User> findByProfileInterestsContainingIgnoreCaseAndAgeGreaterThanOrderByProfileLocationAsc(String interest, int age) {
        List<User> result = new ArrayList<>();

        for (User user : users) {
            if (user.getProfile() != null && user.getProfile().getInterests().toLowerCase().contains(interest.toLowerCase()) && user.getAge() > age) {
                result.add(user);
            }
        }

        for (int i = 0; i < result.size(); i++) {
            for (int j = i + 1; j < result.size(); j++) {
                if (result.get(i).getProfile().getLocation().compareTo(result.get(j).getProfile().getLocation()) > 0) {
                    User temporaryUser = result.get(i);
                    result.set(i, result.get(j));
                    result.set(j, temporaryUser);
                }
            }
        }

        return result;
    }

    @Override
    public <S extends User> S save(S entity) {
        Optional<User> existingUser = findByEmail(entity.getEmail());

        if (existingUser.isPresent()) {
            users.remove(existingUser.get());
        }

        users.add(entity);
        return entity;
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public void delete(User entity) {
        Iterator<User> iterator = users.iterator();

        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getEmail().equals(entity.getEmail())) {
                iterator.remove();
            }
        }
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException("Unimplemented method 'flush'");
    }

    @Override
    public <S extends User> S saveAndFlush(S entity) {
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }

    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }

    @Override
    public void deleteAllInBatch(Iterable<User> entities) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllByIdInBatch'");
    }

    @Override
    public void deleteAllInBatch() {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
    }

    @Override
    public User getOne(Long aLong) {
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public User getById(Long aLong) {
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public User getReferenceById(Long aLong) {
        throw new UnsupportedOperationException("Unimplemented method 'getReferenceById'");
    }

    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends User> long count(Example<S> example) {
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public <S extends User> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        throw new UnsupportedOperationException("Unimplemented method 'findBy'");
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public Optional<User> findById(Long aLong) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public boolean existsById(Long aLong) {
        throw new UnsupportedOperationException("Unimplemented method 'existsById'");
    }

    @Override
    public List<User> findAllById(Iterable<Long> longs) {
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public void deleteById(Long aLong) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public List<User> findAll(Sort sort) {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }
}
