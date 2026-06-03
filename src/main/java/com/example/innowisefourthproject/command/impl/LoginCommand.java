package com.example.innowisefourthproject.command.impl;

import com.example.innowisefourthproject.command.Command;
import com.example.innowisefourthproject.exception.CommandException;
import com.example.innowisefourthproject.exception.DaoException;
import com.example.innowisefourthproject.exception.ServiceException;
import com.example.innowisefourthproject.service.UserService;
import com.example.innowisefourthproject.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class LoginCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String login = request.getParameter("login");
        String password = request.getParameter("pass");
        UserService userService = UserServiceImpl.getInstance();
        HttpSession session = request.getSession();
        String page;
        try {
            if (userService.authenticate(login, password)) {
                request.setAttribute("user", login);
                session.setAttribute("user_name", login);
                page = "pages/main.jsp";
            } else {
                request.setAttribute("login_msg", "incorrect login or password");
                page = "index.jsp";
            }
            session.setAttribute("current_page", page);
        } catch (DaoException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
