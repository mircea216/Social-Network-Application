package com.example.social_network_bastille.repository;

import com.example.social_network_bastille.domain.Entity;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.domain.validators.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private final Map<ID, E> entities;
    private final Validator<E> validator;

    public InMemoryRepository(Validator<E> validator) {
        entities = new HashMap<>();
        this.validator = validator;
    }

    @Override
    public E findOne(ID id) {
        if (id == null)
            throw new IllegalArgumentException("The id must not be null!");
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) throws IllegalArgumentException, IllegalFriendshipException {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null!");
        validator.validate(entity);
        if (entities.get(entity.getId()) != null)
            return entity;
        entities.put(entity.getId(), entity);
        return null;
    }

    @Override
    public E delete(ID id) throws IllegalFriendshipException {
        if (id == null)
            throw new IllegalArgumentException("The id must not be null!");
        if (entities.get(id) != null) {
            return entities.remove(id);
        }
        return null;
    }

    @Override
    public E update(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("The entity must not be null!");
        entities.put(entity.getId(), entity);
        if (entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
            return null;
        }
        return entity;
    }

    @Override
    public int size() {
        List<E> list = new ArrayList<>();
        for (E e : findAll())
            list.add(e);
        return list.size();
    }
}
