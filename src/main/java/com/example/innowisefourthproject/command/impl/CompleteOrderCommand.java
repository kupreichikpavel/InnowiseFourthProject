package com.example.innowisefourthproject.command.impl;

import com.example.innowisefourthproject.command.Command;
import com.example.innowisefourthproject.entity.User;
import com.example.innowisefourthproject.exception.CommandException;
import com.example.innowisefourthproject.exception.ServiceException;
import com.example.innowisefourthproject.service.OrderService;
import com.example.innowisefourthproject.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class CompleteOrderCommand implements Command {
    private static final String PARAM_ORDER_ID = "order_id";

    private static final String INDEX_PAGE = "index.jsp";
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

        String orderId = request.getParameter(PARAM_ORDER_ID);
        HttpSession session = request.getSession();

        try {
            boolean completed = orderService.completeOrder(user, orderId);

            if (completed) {
                session.setAttribute(ORDER_MESSAGE_ATTRIBUTE, "Order was completed");
            } else {
                session.setAttribute(ORDER_MESSAGE_ATTRIBUTE, "Order was not completed");
            }

            return REDIRECT_SHOW_ORDERS;
        } catch (ServiceException e) {
            session.setAttribute(ORDER_MESSAGE_ATTRIBUTE, e.getMessage());
            return REDIRECT_SHOW_ORDERS;
        }
    }
}