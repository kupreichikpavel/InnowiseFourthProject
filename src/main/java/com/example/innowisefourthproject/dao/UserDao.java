package com.example.innowisefourthproject.dao;

import com.example.innowisefourthproject.exception.DaoException;

public interface UserDao {
    boolean authenticate(String login, String password) throws DaoException;
}
