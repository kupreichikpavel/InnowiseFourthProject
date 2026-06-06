package com.example.innowisefourthproject.command.impl;

import com.example.innowisefourthproject.command.Command;
import com.example.innowisefourthproject.exception.CommandException;
import com.example.innowisefourthproject.exception.ServiceException;
import com.example.innowisefourthproject.service.UserService;
import com.example.innowisefourthproject.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class AddUserCommand implements Command {
    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_NAME = "name";

    private static final String REGISTER_PAGE = "pages/register.jsp";
    private static final String REDIRECT_INDEX = "redirect:/index.jsp";

    private static final String REGISTER_MESSAGE_ATTRIBUTE = "register_msg";

    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String login = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);
        String name = request.getParameter(PARAM_NAME);

        try {
            boolean registered = userService.register(login, password, name);

            if (registered) {
                HttpSession session = request.getSession();
                session.setAttribute(REGISTER_MESSAGE_ATTRIBUTE, "Registration completed. Please sign in.");
                return REDIRECT_INDEX;
            }

            request.setAttribute(REGISTER_MESSAGE_ATTRIBUTE, "User with this login already exists");
            return REGISTER_PAGE;
        } catch (ServiceException e) {
            request.setAttribute(REGISTER_MESSAGE_ATTRIBUTE, e.getMessage());
            return REGISTER_PAGE;
        }
    }
}