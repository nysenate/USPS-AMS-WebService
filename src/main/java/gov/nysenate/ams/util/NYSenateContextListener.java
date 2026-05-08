package gov.nysenate.ams.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NYSenateContextListener is used to call initialization methods when the context is (re)deployed and
 * perform cleanup when the context is being shut down. Placed in web.xml to manage Application entities.
 * <p>
 * The Application class must implement the following two static methods:
 * <p>
 *   * boolean bootstrap() - returns false on failure
 *   * boolean shutdown() - returns false on failure to free resources
 *
 */
@WebListener
public class NYSenateContextListener implements ServletContextListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String appClassName = "";
    public Class<?> appClass = null;
    Method bootstrap = null;
    Method shutdown = null;

    public NYSenateContextListener() {}

    /**
     * Starting up context
     * This method is invoked when the Servlet Context (the Web application) is (re)deployed.
     */
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Servlet Context Listener started.");

        try {
            appClassName = sce.getServletContext().getInitParameter("Application");
            appClass = Class.forName(appClassName);

            bootstrap = appClass.getMethod("bootstrap");
            shutdown = appClass.getMethod("shutdown");

            /* Build instances, initialize cache, and set the init attribute to true if succeeded */
            boolean buildStatus = (Boolean)bootstrap.invoke(null); // null ignored for static methods
            logger.info("Bootstrapped using ApplicationFactory: {}", buildStatus);
            sce.getServletContext().setAttribute("init", buildStatus);
        }
        catch (NoSuchMethodException e) {
            logger.error("Factory class: {} must implement static boolean boostrap() and boolean shutdown() methods.", appClassName, e);
        }
        catch (ClassNotFoundException e) {
            logger.error("Factory class: {} not found.", appClassName, e);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            logger.error("Unable to call {}.{}", appClassName, bootstrap.getName(), e);
        }
    }

    /**
     * Shutting down context
     * This method is invoked when the Servlet Context (the Web application) is undeployed or
     * Application Server shuts down.
     */
    public void contextDestroyed(ServletContextEvent sce) {
        if (shutdown != null) {
            logger.info("Closing application resources.");
            try {
                shutdown.invoke(null);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                logger.error("Unable to call {}.{}", appClassName, shutdown.getName(), e);
            }
        }
    }
}
