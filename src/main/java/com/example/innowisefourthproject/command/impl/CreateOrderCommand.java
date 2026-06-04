package com.example.innowisefourthproject.command.impl;

import com.example.innowisefourthproject.command.Command;
import com.example.innowisefourthproject.entity.Order;
import com.example.innowisefourthproject.entity.User;
import com.example.innowisefourthproject.exception.CommandException;
import com.example.innowisefourthproject.exception.ServiceException;
import com.example.innowisefourthproject.service.OrderService;
import com.example.innowisefourthproject.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CreateOrderCommand implements Command {
    private static final Logger logger = LogManager.getLogger(CreateOrderCommand.class);

    private static final String PARAM_ITEM_ID = "item_id";

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

        String itemId = request.getParameter(PARAM_ITEM_ID);

        try {
            boolean created = orderService.createOrder(user, itemId);

            if (created) {
                request.setAttribute(ORDER_MESSAGE_ATTRIBUTE, "Order was created successfully");
            } else {
                request.setAttribute(ORDER_MESSAGE_ATTRIBUTE, "Order was not created");
            }

            List<Order> orders = orderService.findOrders(user);
            request.setAttribute(ORDERS_ATTRIBUTE, orders);

            logger.info("CreateOrderCommand returns page: {}", ORDERS_PAGE);
            return ORDERS_PAGE;
        } catch (ServiceException e) {
            logger.error("Create order command failed", e);
            request.setAttribute(ORDER_MESSAGE_ATTRIBUTE, e.getMessage());

            try {
                List<Order> orders = orderService.findOrders(user);
                request.setAttribute(ORDERS_ATTRIBUTE, orders);

                return ORDERS_PAGE;
            } catch (ServiceException serviceException) {
                throw new CommandException("Could not load orders after create order error", serviceException);
            }
        }
    }
}