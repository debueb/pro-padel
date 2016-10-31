/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.listener;

import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.db.dao.BookingBaseDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Delete all unpaid blocking bookings whose session timeout has expired
 * @author dominik
 */
public class SessionEventListener implements HttpSessionListener{
    
    Logger LOG = Logger.getLogger(SessionEventListener.class);
    
    @Autowired
    BookingBaseDAOI bookingBaseDAO;
    
    @Autowired
    SessionUtil sessionUtil;
    
    private static final Set<PaymentMethod> PAYMENT_METHODS_THAT_DO_NOT_REQUIRE_PAYMENT = EnumSet.of(PaymentMethod.Cash, PaymentMethod.Reservation);
        
 
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        LOG.info("session created: "+se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        LOG.info("session destroyed: "+se.getSession().getId());
        initDependencies(se.getSession().getServletContext());
        LocalDateTime now = new LocalDateTime();
        Booking booking = sessionUtil.getBooking(se.getSession());
        if (booking!=null){
            cancelBooking(booking, now);
        }
        
        //also look for other blocking bookings that might no have been deleted
        LocalDateTime maxAge = now.minusSeconds(se.getSession().getMaxInactiveInterval());
        LOG.info("Looking for unpaid blocking bookings before "+maxAge);
        List<Booking> findBlockedBookings = bookingBaseDAO.findUnpaidBlockingBookings();
        for (Booking blockingBooking : findBlockedBookings) {
            cancelBooking(blockingBooking, maxAge);
        }
    }

    private void cancelBooking(Booking booking, LocalDateTime maxAge) {
        //cancel if the payment method is not cash/reservation and the payment has not been done
        if (booking.getPaymentMethod() == null || (!PAYMENT_METHODS_THAT_DO_NOT_REQUIRE_PAYMENT.contains(booking.getPaymentMethod()) && !booking.getPaymentConfirmed())){
            LocalDateTime blockingTime = booking.getBlockingTime();
            if (blockingTime!=null && blockingTime.isBefore(maxAge)){
                LOG.info("Cancelling booking [user="+booking.getPlayer().toString()+", date="+booking.getBookingDate()+", time="+booking.getBookingTime()+"] due to session timeout");
                bookingBaseDAO.cancelBooking(booking);
            }
        }
    }

    private void initDependencies(ServletContext servletContext) {
        if (sessionUtil == null){
            WebApplicationContextUtils.getWebApplicationContext(servletContext).getAutowireCapableBeanFactory().autowireBean(this);
        }
    }
}
