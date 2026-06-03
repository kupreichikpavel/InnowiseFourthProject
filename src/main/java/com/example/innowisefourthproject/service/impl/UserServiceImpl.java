package com.example.innowisefourthproject.service.impl;

import com.example.innowisefourthproject.dao.impl.UserDaoImpl;
import com.example.innowisefourthproject.entity.User;
import com.example.innowisefourthproject.exception.DaoException;
import com.example.innowisefourthproject.service.UserService;

public class UserServiceImpl implements UserService {
    private static UserServiceImpl instance = new UserServiceImpl();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return instance;
    }

    @Override
    public boolean authenticate(String login, String password) throws DaoException {
        UserDaoImpl userDao = UserDaoImpl.getInstance();
        boolean match = userDao.authenticate(login, password);
        return match;
    }

}
