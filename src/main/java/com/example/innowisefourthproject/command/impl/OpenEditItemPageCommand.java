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

import java.util.Optional;

public class OpenEditItemPageCommand implements Command {
    private static final String PARAM_ID = "id";

    private static final String EDIT_ITEM_PAGE = "pages/edit_item.jsp";
    private static final String ITEMS_PAGE = "pages/items.jsp";

    private static final String ITEM_ATTRIBUTE = "item";
    private static final String ITEM_MESSAGE_ATTRIBUTE = "item_msg";

    private final ItemService itemService = ItemServiceImpl.getInstance();
    private final Logger logger = LogManager.getLogger(OpenEditItemPageCommand.class);

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        if (!CommandUtils.isAdmin(request)) {
            request.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "Access denied");
            CommandUtils.loadItems(request, itemService);
            return ITEMS_PAGE;
        }
        try {
            long id = Long.parseLong(request.getParameter(PARAM_ID));
            Optional<Item> optionalItem = itemService.findById(id);
            if (optionalItem.isPresent()) {
                request.setAttribute(ITEM_ATTRIBUTE, optionalItem.get());
                return EDIT_ITEM_PAGE;
            }
            request.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "Item was not found ");
            return ITEMS_PAGE;

        } catch (NumberFormatException numberFormatException) {
            logger.error("Incorrect item id");
            request.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "Incorrect item id");
            return ITEMS_PAGE;
        } catch (ServiceException e) {
            throw new CommandException("OpenEditPageCommand failed", e);
        }
    }
}
