/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus;

import de.appsolve.padelcampus.utils.HtmlResourceUtil;
import org.apache.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author dominik
 */
@Component
public class ApplicationInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOG = Logger.getLogger(ApplicationInitializer.class);

    @Autowired
    Flyway flyway;

    @Autowired
    HtmlResourceUtil htmlResourceUtil;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        /**
         * Spring Boot auto configuration initializes Flyway before Hibernate. We prevent this by setting
         * flyway.enabled=false in application.properties and manually initialize Flyway after Spring has initialized
         * Hibernate in order to have lightweight schema changes applied automatically by setting
         * spring.jpa.hibernate.ddl-auto=update
         */
        //do database migrations if necessary
        for (MigrationInfo i : flyway.info().all()) {
            LOG.info("migrate task: " + i.getVersion() + " : " + i.getDescription() + " from file: " + i.getScript());
        }
        flyway.migrate();


        /**
         * generate customer specific CSS
         */
        WebApplicationContext ctx = (WebApplicationContext) event.getApplicationContext();
        try {
            htmlResourceUtil.updateCss(ctx.getServletContext());
        } catch (Exception ex) {
            LOG.warn(ex);
        }
    }
}
