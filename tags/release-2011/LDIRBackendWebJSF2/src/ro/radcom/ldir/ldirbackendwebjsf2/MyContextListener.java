/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.radcom.ldir.ldirbackendwebjsf2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Administrator
 */
public class MyContextListener implements ServletContextListener {

    private static final Logger log4j = org.apache.log4j.Logger.getLogger(MyContextListener.class.getCanonicalName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String configFilePath = makeWEBINFPath(context, context.getInitParameter("log4j.config.file"));
        Properties propFile = new Properties();

        try {
            propFile.load(new FileInputStream(configFilePath));

            PropertyConfigurator.configure(propFile);
            log4j.info("Apache Log4J successfuly initialised!");
            log4j.info("AppListener --- contextInitialized");
        } catch (FileNotFoundException ex) {
            log4j.warn("Log4j property file not found! " + ex);
        } catch (IOException ioex) {
            log4j.warn("Cannot read from Log4j property file! " + ioex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        log4j.info("--- contextDestroyed");
    }

    private String makeWEBINFPath(ServletContext context, String path) {
        String tmp = path.trim();
        File file = new File(tmp);
        if (!file.isAbsolute()) {
            tmp = context.getRealPath("/") + "WEB-INF" + File.separator + tmp;
        }
        return tmp;
    }
}
