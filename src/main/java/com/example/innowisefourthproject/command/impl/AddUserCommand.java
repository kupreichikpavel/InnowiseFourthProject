package com.example.innowisefourthproject.command.impl;

import com.example.innowisefourthproject.command.Command;
import com.example.innowisefourthproject.exception.ServiceException;
import com.example.innowisefourthproject.service.UserService;
import com.example.innowisefourthproject.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

public class AddUserCommand implements Command {

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "pass";
    private static final String PARAM_NAME = "name";

    private static final String REGISTER_MESSAGE_ATTRIBUTE = "register_msg";

    private static final String INDEX_PAGE = "index.jsp";
    private static final String REGISTER_PAGE = "pages/register.jsp";
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);
        String name = request.getParameter(PARAM_NAME);

        try {
            boolean registered = userService.register(login, password, name);
            if (registered) {
                request.setAttribute(REGISTER_MESSAGE_ATTRIBUTE,
                        "Registration completed. Please sign in.");
                return INDEX_PAGE;
            }
            request.setAttribute(REGISTER_MESSAGE_ATTRIBUTE, "User with this login is already exists");
            return REGISTER_PAGE;


        } catch (ServiceException e) {
            request.setAttribute(REGISTER_MESSAGE_ATTRIBUTE, e.getMessage());
            return REGISTER_PAGE;
        }
    }
}
