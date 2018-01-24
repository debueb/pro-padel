/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.BookingMonitor;
import de.appsolve.padelcampus.db.model.Player;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;

/**
 * @author dominik
 */
public interface BookingMonitorDAOI extends BaseEntityDAOI<BookingMonitor> {

    List<BookingMonitor> findByPlayerAndDateAndTime(Player player, LocalDate date, LocalTime time);

    List<BookingMonitor> findByDateAndTime(LocalDate date, LocalTime time);
}
