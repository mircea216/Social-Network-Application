package com.example.social_network_bastille.domain.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}