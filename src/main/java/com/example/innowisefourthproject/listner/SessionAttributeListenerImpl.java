package com.example.innowisefourthproject.listner;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

@WebListener
public class SessionAttributeListenerImpl implements HttpSessionAttributeListener {
    static Logger logger = LogManager.getLogger();

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        logger.log(Level.INFO, "+++<<<------->  attributeAdded:" + event.getSession().getAttribute("user_name"));
        logger.log(Level.INFO, "+++<<<------->  attributeAdded:" + event.getSession().getAttribute("current_page"));

    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        logger.log(Level.INFO, "<<<------->  attributeRemoved:" + event.getSession().getAttribute("user_name"));
        logger.log(Level.INFO, "<<<------->  attributeRemoved:" + event.getSession().getAttribute("current_page"));

    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        logger.log(Level.INFO, "###<<<------->  attributeReplaced:" + event.getSession().getAttribute("user_name"));
        logger.log(Level.INFO, "###<<<------->  attributeReplaced:" + event.getSession().getAttribute("current_page"));

    }
}
