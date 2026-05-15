package be.ucll.unit.repository;

import be.ucll.model.Profile;
import be.ucll.repository.ProfileRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ProfileRepositoryStub implements ProfileRepository {

    @Override
    public <S extends Profile> S save(S entity) {
        return entity;
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException("Unimplemented method 'flush'");
    }

    @Override
    public <S extends Profile> S saveAndFlush(S entity) {
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }

    @Override
    public <S extends Profile> List<S> saveAllAndFlush(Iterable<S> entities) {
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }

    @Override
    public void deleteAllInBatch(Iterable<Profile> entities) {
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
    public Profile getOne(Long aLong) {
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public Profile getById(Long aLong) {
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public Profile getReferenceById(Long aLong) {
        throw new UnsupportedOperationException("Unimplemented method 'getReferenceById'");
    }

    @Override
    public <S extends Profile> Optional<S> findOne(Example<S> example) {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public <S extends Profile> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Profile> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Profile> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Profile> long count(Example<S> example) {
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public <S extends Profile> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public <S extends Profile, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        throw new UnsupportedOperationException("Unimplemented method 'findBy'");
    }

    @Override
    public <S extends Profile> List<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public Optional<Profile> findById(Long aLong) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public boolean existsById(Long aLong) {
        throw new UnsupportedOperationException("Unimplemented method 'existsById'");
    }

    @Override
    public List<Profile> findAll() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public List<Profile> findAllById(Iterable<Long> longs) {
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
    public void delete(Profile entity) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public void deleteAll(Iterable<? extends Profile> entities) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public List<Profile> findAll(Sort sort) {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Page<Profile> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }
}
