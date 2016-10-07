/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.web;

import fr.trendev.bean.ActiveSessionTracker;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application lifecycle listener.
 *
 * @author jsie
 */
public class NewServletListener implements ServletContextListener, HttpSessionListener {

    private static final Logger LOG = Logger.getLogger(NewServletListener.class.getName()); 
    
    @EJB
    private ActiveSessionTracker tracker;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.log(Level.INFO, "contextInitialized: {0}", LocalDateTime.now().toString());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.log(Level.INFO, "contextDestroyed: {0}", LocalDateTime.now().toString());
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        
        LOG.log(Level.INFO, "sessionCreated {0}: {1}", new Object[]{se.getSession().getId(), LocalDateTime.now().toString()});
        tracker.add(se.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        LOG.log(Level.INFO, "sessionDestroyed {0}: {1}", new Object[]{se.getSession().getId(), LocalDateTime.now().toString()});
        tracker.remove(se.getSession());
    }
}
