/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.tasks;

import de.appsolve.padelcampus.db.dao.BookingMonitorBaseDAOI;
import de.appsolve.padelcampus.db.model.BookingMonitor;
import de.appsolve.padelcampus.reporting.ErrorReporter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dominik
 */
@Component
public class BookingMonitorCleanupTask {

    private static final Logger LOG = Logger.getLogger(BookingMonitorCleanupTask.class);

    @Autowired
    BookingMonitorBaseDAOI bookingMonitorBaseDAO;

    @Autowired
    ErrorReporter errorReporter;

    @Scheduled(cron = "0 0 3 * * *") //second minute hour day month year, * = any, */5 = every 5
    public void deleteOldBookingMonitors() {
        try {
            List<BookingMonitor> oldBookingMonitors = bookingMonitorBaseDAO.findOldBookingMonitors();
            LOG.info("Deleting " + oldBookingMonitors.size() + " old booking monitors");
            for (BookingMonitor monitor : oldBookingMonitors) {
                bookingMonitorBaseDAO.deleteById(monitor.getId());
            }
        } catch (Throwable t) {
            errorReporter.notify(t);
        }
    }

}
