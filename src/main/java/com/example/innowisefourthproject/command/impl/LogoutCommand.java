package com.example.innowisefourthproject.command.impl;

import com.example.innowisefourthproject.command.Command;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class LogoutCommand implements Command {
    private static final String INDEX_PAGE = "index.jsp";

    @Override
    public String execute(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return INDEX_PAGE;
    }
}
