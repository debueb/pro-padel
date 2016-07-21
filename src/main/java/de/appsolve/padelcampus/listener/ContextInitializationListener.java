/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.listener;

import de.appsolve.padelcampus.utils.HtmlResourceUtil;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;

/**
 *
 * @author dominik
 */
public class ContextInitializationListener implements InitializingBean, ServletContextAware{

    private static final Logger LOG = Logger.getLogger(ContextInitializationListener.class);
    
    private ServletContext servletContext;
    
    @Autowired
    DataSource dataSource;
    
    @Autowired
    HtmlResourceUtil htmlResourceUtil;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        //do database migrations if necessary
        Flyway flyway = new Flyway();
        flyway.setBaselineOnMigrate(true);
        flyway.setDataSource(dataSource);
        for (MigrationInfo i : flyway.info().all()) {
            LOG.info("migrate task: " + i.getVersion() + " : " + i.getDescription() + " from file: " + i.getScript());
        }
        flyway.migrate();
        
        try {
            htmlResourceUtil.updateCss(servletContext);
        } catch (Exception ex) {
            LOG.warn(ex);
        }
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
