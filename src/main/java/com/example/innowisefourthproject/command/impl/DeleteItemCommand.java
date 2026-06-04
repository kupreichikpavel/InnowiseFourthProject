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

public class DeleteItemCommand implements Command {
    private static final String PARAM_ID = "id";

    private static final String ITEMS_PAGE = "pages/items.jsp";

    private static final String ITEM_MESSAGE_ATTRIBUTE = "item_msg";
    private static final String ITEMS_ATTRIBUTE = "items";

    private final ItemService itemService = ItemServiceImpl.getInstance();
    private static final Logger logger = LogManager.getLogger(DeleteItemCommand.class);

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        if (!CommandUtils.isAdmin(request)) {
            request.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "Access denied");
            CommandUtils.loadItems(request, itemService);
            return ITEMS_PAGE;
        }
        try {
            long id = Long.parseLong(request.getParameter(PARAM_ID));
            boolean deleted = itemService.delete(id);
            if (deleted) {
                request.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "It was deleted success");
            } else {
                request.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "It wasn't deleted");
            }
            request.setAttribute(ITEMS_ATTRIBUTE, itemService.findAll());
            return ITEMS_PAGE;
        } catch (NumberFormatException e) {
            request.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "Incorrect id");
            return ITEMS_PAGE;
        } catch (ServiceException e) {
            logger.error("Error deleting item by id");
            loadItems(request);
            return ITEMS_PAGE;
        }
    }

    private void loadItems(HttpServletRequest request) throws CommandException {
        try {
            List<Item> items = itemService.findAll();
            request.setAttribute(ITEMS_ATTRIBUTE, items);
        } catch (ServiceException serviceException) {
            throw new CommandException("Delete item command failed", serviceException);
        }
    }
}
