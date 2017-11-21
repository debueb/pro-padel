/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.tasks;

import de.appsolve.padelcampus.db.dao.EventBaseDAOI;
import de.appsolve.padelcampus.db.model.Event;
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
public class EventDeactivationTask {

  private static final Logger LOG = Logger.getLogger(EventDeactivationTask.class);

  @Autowired
  EventBaseDAOI eventBaseDAO;

  @Autowired
  ErrorReporter errorReporter;

  @Scheduled(cron = "0 5 2 * * *") //second minute hour day month year, * = any, */5 = every 5
  public void deactivateOldEvents() {
    try {
      LocalDate now = new LocalDate();
      LocalDate expiredDate = now.minusWeeks(3);
      List<Event> oldEvents = eventBaseDAO.findActiveEventsExpiredBefore(expiredDate);
      LOG.info("Deactivating " + oldEvents.size() + " events ended before " + expiredDate);
      for (Event event : oldEvents) {
        event.setActive(Boolean.FALSE);
        eventBaseDAO.saveOrUpdate(event);
      }
    } catch (Throwable t) {
      errorReporter.notify(t);
    }
  }

}
