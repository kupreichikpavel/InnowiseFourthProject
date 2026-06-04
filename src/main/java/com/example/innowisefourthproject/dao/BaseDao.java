package com.example.innowisefourthproject.dao;

import com.example.innowisefourthproject.entity.AbstractEntity;
import com.example.innowisefourthproject.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T extends AbstractEntity> {
    boolean insert(T entity) throws DaoException;

    boolean delete(long id) throws DaoException;

    List<T> findAll() throws DaoException;

    Optional<T> findById(long id) throws DaoException;

    boolean update(T entity) throws DaoException;
}
