package com.example.innowisefourthproject.service;

import com.example.innowisefourthproject.exception.DaoException;

public interface UserService  {
     boolean authenticate(String login, String password) throws DaoException;
}
