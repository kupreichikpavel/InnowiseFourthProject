package com.example.innowisefourthproject.command.impl;

import com.example.innowisefourthproject.entity.Item;
import com.example.innowisefourthproject.entity.User;
import com.example.innowisefourthproject.exception.CommandException;
import com.example.innowisefourthproject.exception.ServiceException;
import com.example.innowisefourthproject.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public class CommandUtils {
    private static final String USER_ATTRIBUTE = "user";
    private static final String ITEMS_ATTRIBUTE = "items";

    private CommandUtils() {
    }

    public static boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return false;
        }

        Object userObject = session.getAttribute(USER_ATTRIBUTE);

        if (!(userObject instanceof User)) {
            return false;
        }

        User user = (User) userObject;

        return user.isAdmin();
    }

    public static void loadItems(HttpServletRequest request, ItemService itemService) throws CommandException {
        try {
            List<Item> items = itemService.findAll();
            request.setAttribute(ITEMS_ATTRIBUTE, items);
        } catch (ServiceException e) {
            throw new CommandException("Could not load items", e);
        }
    }

    public static User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object userObject = session.getAttribute("user");

        if (!(userObject instanceof User)) {
            return null;
        }

        return (User) userObject;
    }
}
