/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.model.Booking;
import java.util.List;
import org.joda.time.LocalDate;

/**
 *
 * @author dominik
 */
public interface BookingDAOI extends GenericDAOI<Booking>{
    
    public Booking findByUUID(String UUID);
    
    public List<Booking> findBlockedBookingsForDate(LocalDate date);
    public List<Booking> findBlockedBookings();
    public List<Booking> findBetween(LocalDate startDate, LocalDate endDate);
    public List<Booking> findReservations();
}
