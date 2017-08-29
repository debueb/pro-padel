/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.tasks;

import de.appsolve.padelcampus.db.dao.LoginCookieBaseDAOI;
import de.appsolve.padelcampus.db.model.LoginCookie;
import de.appsolve.padelcampus.reporting.ErrorReporter;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dominik
 */
@Component
public class LoginCookieCleanupTask {

    private static final Logger LOG = Logger.getLogger(LoginCookieCleanupTask.class);

    @Autowired
    LoginCookieBaseDAOI loginCookieBaseDAO;

    @Autowired
    ErrorReporter errorReporter;

    @Scheduled(cron = "0 0 1 * * *") //second minute hour day month year, * = any, */5 = every 5
    public void deleteOldVouchers() {
        try {
            LocalDate now = new LocalDate();
            List<LoginCookie> expiredCookies = loginCookieBaseDAO.findExpiredBefore(now);
            LOG.info("Deleting " + expiredCookies.size() + " login cookies expired before " + now);
            for (LoginCookie cookie : expiredCookies) {
                loginCookieBaseDAO.deleteById(cookie.getId());
            }
        } catch (Throwable t) {
            errorReporter.notify(t);
        }
    }
}
