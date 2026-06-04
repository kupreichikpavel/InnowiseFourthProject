package com.example.innowisefourthproject.command;

import com.example.innowisefourthproject.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;

public interface Command {
    String execute(HttpServletRequest request) throws CommandException;
}
