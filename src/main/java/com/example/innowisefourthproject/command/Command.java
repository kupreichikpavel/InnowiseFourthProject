package com.example.innowisefourthproject.command;

import com.example.innowisefourthproject.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Command {
    String execute(HttpServletRequest request) throws CommandException;
    default void refresh(){}
}
