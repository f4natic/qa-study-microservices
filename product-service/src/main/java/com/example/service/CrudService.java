package com.example.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudService<T> {

    Long getTotal();
    Page<T> findAll(Pageable pageable);
    T findByName(String name);
    T create(T t);
    T update(String name, T t);
    void delete(String name);
}
