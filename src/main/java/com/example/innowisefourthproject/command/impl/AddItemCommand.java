package com.example.innowisefourthproject.command.impl;

import com.example.innowisefourthproject.command.Command;
import com.example.innowisefourthproject.entity.User;
import com.example.innowisefourthproject.exception.CommandException;
import com.example.innowisefourthproject.exception.ServiceException;
import com.example.innowisefourthproject.service.ItemService;
import com.example.innowisefourthproject.service.impl.ItemServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class AddItemCommand implements Command {
    private static final String PARAM_NAME = "name";
    private static final String PARAM_DESCRIPTION = "description";
    private static final String PARAM_PRICE = "price";

    private static final String INDEX_PAGE = "index.jsp";
    private static final String REDIRECT_SHOW_ITEMS = "redirect:/controller?command=show_items";

    private static final String ITEM_MESSAGE_ATTRIBUTE = "item_msg";

    private final ItemService itemService = ItemServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        User user = CommandUtils.getCurrentUser(request);

        if (user == null) {
            request.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "You must sign in first");
            return INDEX_PAGE;
        }

        HttpSession session = request.getSession();

        if (!user.isAdmin()) {
            session.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "Access denied");
            return REDIRECT_SHOW_ITEMS;
        }

        String name = request.getParameter(PARAM_NAME);
        String description = request.getParameter(PARAM_DESCRIPTION);
        String price = request.getParameter(PARAM_PRICE);

        try {
            boolean added = itemService.add(name, description, price);

            if (added) {
                session.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "Item was added successfully");
            } else {
                session.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "Item was not added");
            }

            return REDIRECT_SHOW_ITEMS;
        } catch (ServiceException e) {
            session.setAttribute(ITEM_MESSAGE_ATTRIBUTE, e.getMessage());
            return REDIRECT_SHOW_ITEMS;
        }
    }
}