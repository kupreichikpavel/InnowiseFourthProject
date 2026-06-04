package com.example.innowisefourthproject.dao;

import com.example.innowisefourthproject.entity.User;
import com.example.innowisefourthproject.exception.DaoException;

import java.util.Optional;

public interface UserDao extends BaseDao<User> {

    Optional<User> findByLogin(String login) throws DaoException;

    boolean existsByLogin(String login) throws DaoException;
}