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

public class ShowItemsCommand implements Command {


    private static final String ITEMS_PAGE = "pages/items.jsp";
    private static final String ITEMS_ATTRIBUTE = "items";

    private static final Logger logger = LogManager.getLogger(ShowItemsCommand.class);
    private final ItemService itemService = ItemServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        try {
            List<Item> itemList = itemService.findAll();
            request.setAttribute(ITEMS_ATTRIBUTE, itemList);
            return ITEMS_PAGE;
        } catch (ServiceException e) {
            logger.error("Error of execute info about items ");
            throw new CommandException("Failed to execute info about items ", e);
        }
    }
}
