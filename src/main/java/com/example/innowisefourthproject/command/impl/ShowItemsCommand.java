package com.example.innowisefourthproject.command.impl;

import com.example.innowisefourthproject.command.Command;
import com.example.innowisefourthproject.entity.Item;
import com.example.innowisefourthproject.exception.CommandException;
import com.example.innowisefourthproject.exception.ServiceException;
import com.example.innowisefourthproject.service.ItemService;
import com.example.innowisefourthproject.service.impl.ItemServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public class ShowItemsCommand implements Command {
    private static final String ITEMS_PAGE = "pages/items.jsp";

    private static final String ITEMS_ATTRIBUTE = "items";
    private static final String ITEM_MESSAGE_ATTRIBUTE = "item_msg";

    private final ItemService itemService = ItemServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        try {
            List<Item> items = itemService.findAll();
            request.setAttribute(ITEMS_ATTRIBUTE, items);

            HttpSession session = request.getSession(false);

            if (session != null) {
                Object message = session.getAttribute(ITEM_MESSAGE_ATTRIBUTE);

                if (message != null) {
                    request.setAttribute(ITEM_MESSAGE_ATTRIBUTE, message);
                    session.removeAttribute(ITEM_MESSAGE_ATTRIBUTE);
                }
            }

            return ITEMS_PAGE;
        } catch (ServiceException e) {
            throw new CommandException("Show items command failed", e);
        }
    }
}