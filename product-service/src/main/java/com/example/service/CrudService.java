package com.example.service;

import java.util.Collection;

public interface CrudService<T> {

    Collection<T> findAll();
    T findByName(String name);
    T create(T t);
    T update(T t);
    boolean delete(long id);
}
