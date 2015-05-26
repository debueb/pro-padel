/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.listener;

import de.appsolve.padelcampus.utils.HtmlResourceUtil;
import de.appsolve.padelcampus.utils.ModuleUtil;
import java.util.logging.Level;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author dominik
 */
public class ContextInitializationListener implements ServletContextListener{

    private static final Logger log = Logger.getLogger(ContextInitializationListener.class);
    
    @Autowired
    DataSource dataSource;
    
    @Autowired
    ModuleUtil moduleUtil;
    
    @Autowired
    HtmlResourceUtil htmlResourceUtil;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //since this is initialized by the servlet container (and not by spring), we need to inject the dependencies manually
        WebApplicationContextUtils
            .getRequiredWebApplicationContext(sce.getServletContext())
            .getAutowireCapableBeanFactory()
            .autowireBean(this);


        //do database migrations if necessary
        Flyway flyway = new Flyway();
        flyway.setInitOnMigrate(true);
        flyway.setDataSource(dataSource);
        for (MigrationInfo i : flyway.info().all()) {
            log.info("migrate task: " + i.getVersion() + " : " + i.getDescription() + " from file: " + i.getScript());
        }
        flyway.migrate();
        
        moduleUtil.reloadModules(sce.getServletContext());
        
        try {
            htmlResourceUtil.updateCss(sce.getServletContext());
        } catch (Exception ex) {
            log.warn(ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //empty
    }
}
