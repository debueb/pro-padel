/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import java.util.List;

/**
 *
 * @author dominik
 */
public interface BookingBaseDAOI extends BaseEntityDAOI<Booking>{
    
    public List<Booking> findBlockedBookings();
    public void cancelBooking(Booking booking);
}
