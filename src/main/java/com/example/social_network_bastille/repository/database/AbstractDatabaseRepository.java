package com.example.social_network_bastille.repository.database;


import com.example.social_network_bastille.domain.Entity;
import com.example.social_network_bastille.domain.validators.Validator;
import com.example.social_network_bastille.repository.InMemoryRepository;

public abstract class AbstractDatabaseRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    protected final String url;
    protected final String username;
    protected final String password;

    public AbstractDatabaseRepository(Validator<E> validator, String url, String username, String password) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
    }
}
