package com.example.innowisefourthproject.command.impl;

import com.example.innowisefourthproject.command.Command;
import jakarta.servlet.http.HttpServletRequest;

public class LogoutCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return "";
    }
}
