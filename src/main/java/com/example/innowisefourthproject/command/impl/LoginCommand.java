package com.example.innowisefourthproject.command.impl;

import com.example.innowisefourthproject.command.Command;
import com.example.innowisefourthproject.entity.User;
import com.example.innowisefourthproject.exception.CommandException;
import com.example.innowisefourthproject.exception.ServiceException;
import com.example.innowisefourthproject.service.UserService;
import com.example.innowisefourthproject.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

public class LoginCommand implements Command {


    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "pass";

    private static final String USER_ATTRIBUTE = "user";
    private static final String ERROR_ATTRIBUTE = "login_msg";

    private static final String MAIN_PAGE = "pages/main.jsp";
    private static final String INDEX_PAGE = "index.jsp";

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String login = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);

        UserService userService = UserServiceImpl.getInstance();
        try {
            Optional<User> userOptional = userService.login(login, password);
            if (userOptional.isPresent()) {
                HttpSession session = request.getSession();
                session.setAttribute(USER_ATTRIBUTE, userOptional.get());
                session.setAttribute("current_page", MAIN_PAGE);
                return MAIN_PAGE;
            }
            request.setAttribute(ERROR_ATTRIBUTE, "Incorrect password or login");
            request.getSession().setAttribute("current_page", INDEX_PAGE);
            return INDEX_PAGE;
        } catch (ServiceException e) {
            throw new CommandException("Login command failed", e);
        }
    }
}
