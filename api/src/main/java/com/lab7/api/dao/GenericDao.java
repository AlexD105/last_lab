package com.lab7.api.dao;

import java.util.List;
import java.util.Stack;

/**
 * Дженерик DAO класс
 * @param <E> - Тип сущности
 * @param <ID> - Тип первичного ключа сущности
 */
public interface GenericDao<E, ID> {

    E save(E entity);

    void saveAll(List<E> entities);

    Stack<E> findAll();

    E findById(ID id);

    boolean update(E entity);

    boolean delete(E entity);

    boolean deleteById(ID id);

    void deleteAll();

}
