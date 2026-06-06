package com.example.innowisefourthproject.api;

import com.example.innowisefourthproject.entity.Item;
import com.example.innowisefourthproject.exception.ServiceException;
import com.example.innowisefourthproject.service.ItemService;
import com.example.innowisefourthproject.service.impl.ItemServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns = "/api/items")
public class ItemsApiServlet extends HttpServlet {
    private final ItemService itemService = ItemServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");


        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        try {
            List<Item> items = itemService.findAll();
            String json = toJson(items);
            try (PrintWriter writer = resp.getWriter()) {
                writer.write(json);
            }
        } catch (ServiceException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter writer = resp.getWriter()) {
                writer.write("{\"error\":\"Could not load items\"}");
            }
        }
    }

    private String toJson(List<Item> items) {
        StringBuilder json = new StringBuilder();

        json.append("[");

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);

            json.append("{");
            json.append("\"id\":").append(item.getId()).append(",");
            json.append("\"name\":\"").append(escapeJson(item.getName())).append("\",");
            json.append("\"description\":\"").append(escapeJson(item.getDescription())).append("\",");
            json.append("\"price\":").append(item.getPrice());
            json.append("}");

            if (i < items.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        return json.toString();
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}