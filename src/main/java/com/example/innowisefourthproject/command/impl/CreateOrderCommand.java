package com.example.innowisefourthproject.command.impl;

import com.example.innowisefourthproject.command.Command;
import com.example.innowisefourthproject.entity.User;
import com.example.innowisefourthproject.exception.CommandException;
import com.example.innowisefourthproject.exception.ServiceException;
import com.example.innowisefourthproject.service.OrderService;
import com.example.innowisefourthproject.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateOrderCommand implements Command {
    private static final Logger logger = LogManager.getLogger(CreateOrderCommand.class);

    private static final String PARAM_ITEM_ID = "item_id";

    private static final String INDEX_PAGE = "index.jsp";
    private static final String ORDERS_PAGE = "pages/orders.jsp";
    private static final String REDIRECT_SHOW_ORDERS = "redirect:/controller?command=show_orders";

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

            HttpSession session = request.getSession();

            if (created) {
                session.setAttribute(ORDER_MESSAGE_ATTRIBUTE, "Order was created successfully");
            } else {
                session.setAttribute(ORDER_MESSAGE_ATTRIBUTE, "Order was not created");
            }

            logger.info("CreateOrderCommand redirects to show_orders");
            return REDIRECT_SHOW_ORDERS;
        } catch (ServiceException e) {
            request.setAttribute(ORDER_MESSAGE_ATTRIBUTE, e.getMessage());

            try {
                request.setAttribute("orders", orderService.findOrders(user));
                return ORDERS_PAGE;
            } catch (ServiceException serviceException) {
                throw new CommandException("Create order command failed", serviceException);
            }
        }
    }
}