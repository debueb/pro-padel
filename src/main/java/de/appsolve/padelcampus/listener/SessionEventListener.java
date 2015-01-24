/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.listener;

import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.utils.SessionUtil;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Delete all unpaid blocking bookings whose session timeout has expired
 * @author dominik
 */
@Component
public class SessionEventListener implements HttpSessionListener, ApplicationContextAware{
    
    Logger log = Logger.getLogger(SessionEventListener.class);
    
    @Autowired
    BookingDAOI bookingDAO;
    
    @Autowired
    SessionUtil sessionUtil;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof WebApplicationContext) {
            WebApplicationContext wac = (WebApplicationContext) applicationContext;
            try {
                wac.getServletContext().addListener(this);
            } catch (UnsupportedOperationException e){
                //MockServletContext throws this, which we can safely ignore
            }
        } else {
            log.info("No web application context given. SessionEventListener will not work.");
        }
    }           
    
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
        for (Booking blockingBooking : bookingDAO.findBlockedBookings()) {
            cancelBooking(blockingBooking, maxAge);
        }
    }

    private void cancelBooking(Booking booking, LocalDateTime maxAge) {
        //if the payment has not been done
        if (!booking.getPaymentConfirmed()){
            LocalDateTime blockingTime = booking.getBlockingTime();
            if (blockingTime!=null && blockingTime.isBefore(maxAge)){
                log.info("Cancelling booking [user="+booking.getPlayer().getDisplayName()+", date="+booking.getBookingDate()+", time="+booking.getBookingTime()+"] due to session timeout");
                booking.setBlockingTime(null);
                booking.setCancelled(true);
                booking.setCancelReason("Session Timeout");
                bookingDAO.saveOrUpdate(booking);
            }
        }
    }
}
