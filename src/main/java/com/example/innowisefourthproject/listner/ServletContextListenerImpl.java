package com.example.innowisefourthproject.listner;

import com.example.innowisefourthproject.pool.ConnectionPool;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@WebListener
public class ServletContextListenerImpl implements ServletContextListener {
    static Logger logger = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.log(Level.INFO,"++++++ contextInitialized : " + sce.getServletContext().getServletContextName());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.log(Level.INFO, "--------contextInitialized : " + sce.getServletContext().getServerInfo());
    }
}
