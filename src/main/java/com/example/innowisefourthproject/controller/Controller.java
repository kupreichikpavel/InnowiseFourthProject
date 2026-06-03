package com.example.innowisefourthproject.controller;

import java.io.*;

import com.example.innowisefourthproject.command.Command;
import com.example.innowisefourthproject.command.CommandType;
import com.example.innowisefourthproject.exception.CommandException;
import com.example.innowisefourthproject.pool.ConnectionPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(name = "helloServlet", urlPatterns = "/controller")
public class Controller extends HttpServlet {
    static Logger logger = LogManager.getLogger();

    public void init() {
        logger.log(Level.INFO, "----->>> Servlet Init" + this.getServletInfo());
        ConnectionPool.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        String commandStr = request.getParameter("command");
        Command command = CommandType.define(commandStr);
        String page = null;
        try {
            page = command.execute(request);
            request.getRequestDispatcher(page).forward(request, response);
        } catch (CommandException e) {
            // 1 response.sendError(500);
            //  2 throw new ServletException(e);
            request.setAttribute("error_,msg", e.getCause());
            request.getRequestDispatcher("pages/error_500.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    public void destroy() {
        ConnectionPool.getInstance().destroyPool();
        logger.log(Level.INFO, "----->>> Servlet Destroyed" + this.getServletInfo());
    }
}