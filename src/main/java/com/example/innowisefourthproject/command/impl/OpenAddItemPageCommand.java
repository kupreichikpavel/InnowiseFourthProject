package com.example.innowisefourthproject.command.impl;

import com.example.innowisefourthproject.command.Command;
import com.example.innowisefourthproject.exception.CommandException;
import com.example.innowisefourthproject.service.ItemService;
import com.example.innowisefourthproject.service.impl.ItemServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

public class OpenAddItemPageCommand implements Command {
    private static final String ADD_ITEM_PAGE = "pages/add_item.jsp";
    private static final String ITEMS_PAGE = "pages/items.jsp";
    private static final String ITEM_MESSAGE_ATTRIBUTE = "item_msg";

    private final ItemService itemService = ItemServiceImpl.getInstance();


    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        if (!CommandUtils.isAdmin(request)) {
            request.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "Access denied");
            CommandUtils.loadItems(request, itemService);
            return ITEMS_PAGE;
        }
        return ADD_ITEM_PAGE;
    }
}
