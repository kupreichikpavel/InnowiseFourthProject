package com.example.innowisefourthproject.command.impl;

import com.example.innowisefourthproject.command.Command;
import com.example.innowisefourthproject.entity.User;
import com.example.innowisefourthproject.exception.CommandException;
import com.example.innowisefourthproject.exception.ServiceException;
import com.example.innowisefourthproject.service.ItemService;
import com.example.innowisefourthproject.service.impl.ItemServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeleteItemCommand implements Command {
    private static final Logger logger = LogManager.getLogger(DeleteItemCommand.class);

    private static final String PARAM_ID = "id";

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

        try {
            long id = Long.parseLong(request.getParameter(PARAM_ID));

            boolean deleted = itemService.delete(id);

            if (deleted) {
                session.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "Item was deleted successfully");
            } else {
                session.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "Item was not deleted");
            }

            return REDIRECT_SHOW_ITEMS;
        } catch (NumberFormatException e) {
            session.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "Incorrect item id");
            return REDIRECT_SHOW_ITEMS;
        } catch (ServiceException e) {
            logger.error("Error deleting item", e);
            session.setAttribute(ITEM_MESSAGE_ATTRIBUTE, "Could not delete item");
            return REDIRECT_SHOW_ITEMS;
        }
    }
}