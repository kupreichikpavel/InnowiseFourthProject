package com.example.innowisefourthproject.command.impl;

import com.example.innowisefourthproject.command.Command;
import com.example.innowisefourthproject.entity.Order;
import com.example.innowisefourthproject.entity.User;
import com.example.innowisefourthproject.exception.CommandException;
import com.example.innowisefourthproject.exception.ServiceException;
import com.example.innowisefourthproject.service.OrderService;
import com.example.innowisefourthproject.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class ShowOrdersCommand implements Command {
    private static final String ORDERS_PAGE = "pages/orders.jsp";
    private static final String INDEX_PAGE = "index.jsp";

    private static final String ORDERS_ATTRIBUTE = "orders";
    private static final String ORDER_MESSAGE_ATTRIBUTE = "order_msg";

    private final OrderService orderService = OrderServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        User user = CommandUtils.getCurrentUser(request);

        if (user == null) {
            request.setAttribute(ORDER_MESSAGE_ATTRIBUTE, "You must sign in first");
            return INDEX_PAGE;
        }

        try {
            List<Order> orders = orderService.findOrders(user);
            request.setAttribute(ORDERS_ATTRIBUTE, orders);

            return ORDERS_PAGE;
        } catch (ServiceException e) {
            throw new CommandException("Show orders command failed", e);
        }
    }
}