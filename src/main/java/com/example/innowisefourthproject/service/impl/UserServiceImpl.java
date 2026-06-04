package com.example.innowisefourthproject.service.impl;

import com.example.innowisefourthproject.dao.UserDao;
import com.example.innowisefourthproject.dao.impl.UserDaoImpl;
import com.example.innowisefourthproject.entity.Role;
import com.example.innowisefourthproject.entity.User;
import com.example.innowisefourthproject.exception.DaoException;
import com.example.innowisefourthproject.exception.ServiceException;
import com.example.innowisefourthproject.service.UserService;
import com.example.innowisefourthproject.util.PasswordEncoder;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final UserServiceImpl INSTANCE = new UserServiceImpl();

    private final UserDao userDao = UserDaoImpl.getInstance();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<User> login(String login, String password) throws ServiceException {
        validateLoginData(login, password);

        try {
            String normalizedLogin = login.trim();

            Optional<User> userOptional = userDao.findByLogin(normalizedLogin);

            if (userOptional.isEmpty()) {
                return Optional.empty();
            }

            User user = userOptional.get();

            boolean passwordCorrect = PasswordEncoder.matches(
                    password,
                    user.getPasswordHash()
            );

            if (passwordCorrect) {
                return Optional.of(user);
            }

            return Optional.empty();
        } catch (DaoException e) {
            throw new ServiceException("Could not login user", e);
        }
    }

    @Override
    public boolean register(String login, String password, String name) throws ServiceException {
        validateRegistrationData(login, password, name);

        try {
            String normalizedLogin = login.trim();

            if (userDao.existsByLogin(normalizedLogin)) {
                return false;
            }

            String passwordHash = PasswordEncoder.encode(password);

            User user = new User(
                    normalizedLogin,
                    passwordHash,
                    name,
                    Role.USER
            );

            return userDao.insert(user);
        } catch (DaoException e) {
            throw new ServiceException("Could not register user", e);
        }
    }

    private void validateLoginData(String login, String password) throws ServiceException {
        if (login == null || login.isBlank()) {
            throw new ServiceException("Login cannot be empty");
        }

        if (password == null || password.isBlank()) {
            throw new ServiceException("Password cannot be empty");
        }
    }

    private void validateRegistrationData(String login, String password, String name) throws ServiceException {
        validateLoginData(login, password);

        if (name == null || name.isBlank()) {
            throw new ServiceException("Name cannot be empty");
        }
    }
}