package com.example.innowisefourthproject.service;

import com.example.innowisefourthproject.entity.User;
import com.example.innowisefourthproject.exception.ServiceException;

import java.util.Optional;

public interface UserService {

    Optional<User> login(String login, String password) throws ServiceException;

    boolean register(String login, String password, String name) throws ServiceException;
}
