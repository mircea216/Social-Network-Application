package com.example.social_network_bastille.repository.file;

import com.example.social_network_bastille.domain.Entity;
import com.example.social_network_bastille.domain.validators.IllegalFriendshipException;
import com.example.social_network_bastille.domain.validators.Validator;
import com.example.social_network_bastille.repository.InMemoryRepository;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    String fileName;

    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    private void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String newLine;
            while ((newLine = reader.readLine()) != null) {
                List<String> data = Arrays.asList(newLine.split(";"));
                E entity = extractEntity(data);
                super.save(entity);
            }
        } catch (IOException | IllegalFriendshipException e) {
            e.printStackTrace();
        }
    }

    /**
     * extract entity  - template method design pattern
     * creates an entity of type E having a specified list of @code attributes
     *
     * @param attributes
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);

    /**
     * Returns the string containing the entity.
     *
     * @param entity
     * @return entity
     */
    protected abstract String createEntityAsString(E entity);

    @Override
    public E save(E entity) throws IllegalArgumentException, IllegalFriendshipException {
        if (super.save(entity) == null) {
            writeToFile(entity);
        }
        return entity;
    }

    @Override
    public E delete(ID id) throws IllegalFriendshipException {
        E e = super.delete(id);
        reloadData();
        return e;
    }

    protected void writeToFile(E entity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(createEntityAsString(entity));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void reloadData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (E e : super.findAll()) {
                writer.write(createEntityAsString(e));
                writer.newLine();
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public int size() {
        return super.size();
    }
}
