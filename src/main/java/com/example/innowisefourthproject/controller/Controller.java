package com.example.innowisefourthproject.controller;

import com.example.innowisefourthproject.command.Command;
import com.example.innowisefourthproject.command.CommandType;
import com.example.innowisefourthproject.exception.CommandException;
import com.example.innowisefourthproject.pool.ConnectionPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet(name = "controller", urlPatterns = "/controller")
public class Controller extends HttpServlet {
    private static final String REDIRECT_PREFIX = "redirect:";
    private static final Logger logger = LogManager.getLogger(Controller.class);

    @Override
    public void init() {
        logger.log(Level.INFO, "Servlet initialized: {}", this.getServletInfo());
        ConnectionPool.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }
    @Override
    public void destroy() {
        ConnectionPool.getInstance().destroyPool();
        logger.log(Level.INFO, "Servlet destroyed: {}", this.getServletInfo());
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String commandStr = request.getParameter("command");
        logger.info("Command from request: {}", commandStr);

        Command command = CommandType.define(commandStr);
        logger.info("Command class: {}", command.getClass().getSimpleName());

        try {
            String page = command.execute(request);
            logger.info("Page after command: {}", page);

            if (page.startsWith(REDIRECT_PREFIX)) {
                String redirectPath = page.substring(REDIRECT_PREFIX.length());
                response.sendRedirect(request.getContextPath() + redirectPath);
                return;
            }

            request.getRequestDispatcher(page).forward(request, response);
        } catch (CommandException e) {
            logger.error("Command execution failed", e);
            request.setAttribute("error_msg", e.getMessage());
            request.getRequestDispatcher("pages/error_500.jsp").forward(request, response);
        }
    }
}