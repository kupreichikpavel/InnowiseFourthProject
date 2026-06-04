package com.example.innowisefourthproject.command.impl;

import com.example.innowisefourthproject.command.Command;
import com.example.innowisefourthproject.entity.Item;
import com.example.innowisefourthproject.exception.CommandException;
import com.example.innowisefourthproject.exception.ServiceException;
import com.example.innowisefourthproject.service.ItemService;
import com.example.innowisefourthproject.service.impl.ItemServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class AddItemCommand implements Command {
    private static final String PARAM_NAME = "name";
    private static final String PARAM_DESCRIPTION = "description";
    private static final String PARAM_PRICE = "price";

    private static final String ADD_ITEM_PAGE = "pages/add_item.jsp";
    private static final String ITEMS_PAGE = "pages/items.jsp";

    private static final String ITEM_MESSAGE_ATTRIBUTE = "item_msg";
    private static final String ITEMS_ATTRIBUTE = "items";

    private final ItemService itemService = ItemServiceImpl.getInstance();
    private static final Logger logger = LogManager.getLogger(AddItemCommand.class);


    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        if (!CommandUtils.isAdmin(request)) {
            request.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "Access denied");
            CommandUtils.loadItems(request, itemService);
            return ITEMS_PAGE;
        }
        String name = request.getParameter(PARAM_NAME);
        String description = request.getParameter(PARAM_DESCRIPTION);
        String price = request.getParameter(PARAM_PRICE);

        try {
            boolean added = itemService.add(name, description, price);

            if (added) {
                request.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "It was added ");
                List<Item> items = itemService.findAll();
                request.setAttribute(ITEMS_ATTRIBUTE, items);
                return ITEMS_PAGE;
            }
            request.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "It wasn't added");
            return ADD_ITEM_PAGE;
        } catch (ServiceException e) {
            logger.error("Error added item ");
            throw new CommandException("Failed adding items", e);
        }
    }
}
