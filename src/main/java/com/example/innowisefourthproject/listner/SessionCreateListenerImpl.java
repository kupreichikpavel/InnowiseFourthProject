package com.example.innowisefourthproject.listner;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class SessionCreateListenerImpl implements  HttpSessionListener{
    static Logger logger = LogManager.getLogger();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.log(Level.INFO,"---------->  sessionCreated: "+ se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.log(Level.INFO,"---------->  sessionDestroyed: "+ se.getSession().getId());
    }
}
