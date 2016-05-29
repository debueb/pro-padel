/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.listener;

import de.appsolve.padelcampus.db.dao.BookingBaseDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.utils.SessionUtil;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Delete all unpaid blocking bookings whose session timeout has expired
 * @author dominik
 */
public class SessionEventListener implements HttpSessionListener{
    
    Logger log = Logger.getLogger(SessionEventListener.class);
    
    @Autowired
    BookingBaseDAOI bookingBaseDAO;
    
    @Autowired
    SessionUtil sessionUtil;
    
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        //empty
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        LocalDateTime now = new LocalDateTime();
        Booking booking = sessionUtil.getBooking(se.getSession());
        if (booking!=null){
            cancelBooking(booking, now);
        }
        
        //also look for other blocking bookings that might no have been deleted
        log.info("Looking for unpaid blocking bookings");
        LocalDateTime maxAge = now.minusSeconds(se.getSession().getMaxInactiveInterval());
        for (Booking blockingBooking : bookingBaseDAO.findBlockedBookings()) {
            cancelBooking(blockingBooking, maxAge);
        }
    }

    private void cancelBooking(Booking booking, LocalDateTime maxAge) {
        //if the payment has not been done
        if (!booking.getPaymentConfirmed()){
            LocalDateTime blockingTime = booking.getBlockingTime();
            if (blockingTime!=null && blockingTime.isBefore(maxAge)){
                log.info("Cancelling booking [user="+booking.getPlayer().toString()+", date="+booking.getBookingDate()+", time="+booking.getBookingTime()+"] due to session timeout");
                bookingBaseDAO.cancelBooking(booking);
            }
        }
    }
}
