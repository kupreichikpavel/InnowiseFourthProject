package com.example.innowisefourthproject.command.impl;

import com.example.innowisefourthproject.command.Command;
import com.example.innowisefourthproject.entity.Item;
import com.example.innowisefourthproject.exception.CommandException;
import com.example.innowisefourthproject.exception.ServiceException;
import com.example.innowisefourthproject.service.ItemService;
import com.example.innowisefourthproject.service.impl.ItemServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class UpdateItemCommand implements Command {
    private static final String PARAM_ID = "id";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_DESCRIPTION = "description";
    private static final String PARAM_PRICE = "price";

    private static final String EDIT_ITEM_PAGE = "pages/edit_item.jsp";
    private static final String ITEMS_PAGE = "pages/items.jsp";

    private static final String ITEM_MESSAGE_ATTRIBUTE = "item_msg";
    private static final String ITEMS_ATTRIBUTE = "items";

    private final ItemService itemService = ItemServiceImpl.getInstance();


    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        if (!CommandUtils.isAdmin(request)) {
            request.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "Access denied");
            CommandUtils.loadItems(request, itemService);
            return ITEMS_PAGE;
        }
        String idString = request.getParameter(PARAM_ID);
        String name = request.getParameter(PARAM_NAME);
        String description = request.getParameter(PARAM_DESCRIPTION);
        String price = request.getParameter(PARAM_PRICE);

        try {
            long id = Long.parseLong(idString);

            boolean updated = itemService.update(id, name, description, price);

            if (updated) {
                request.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "Item was updated successfully");

                List<Item> items = itemService.findAll();
                request.setAttribute(ITEMS_ATTRIBUTE, items);

                return ITEMS_PAGE;
            }

            request.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "Item was not updated");
            return EDIT_ITEM_PAGE;
        } catch (NumberFormatException e) {
            request.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "Incorrect item id");
            return EDIT_ITEM_PAGE;
        } catch (ServiceException e) {
            request.setAttribute(ITEM_MESSAGE_ATTRIBUTE, e.getMessage());
            return EDIT_ITEM_PAGE;
        }
    }
}