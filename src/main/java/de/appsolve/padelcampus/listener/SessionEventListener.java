/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.listener;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import de.appsolve.padelcampus.db.dao.BookingBaseDAOI;
import de.appsolve.padelcampus.db.dao.PayPalConfigBaseDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.PayPalConfig;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Delete all unpaid blocking bookings whose session timeout has expired
 *
 * @author dominik
 */
public class SessionEventListener implements HttpSessionListener {

    private static final Logger LOG = Logger.getLogger(SessionEventListener.class);

    private static int activeSessions = 0;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private BookingBaseDAOI bookingBaseDAO;

    @Autowired
    private PayPalConfigBaseDAOI payPalConfigBaseDAO;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        activeSessions++;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        if (activeSessions > 0) {
            activeSessions--;
        }
        initDependencies(se.getSession().getServletContext());
        LocalDateTime now = new LocalDateTime();
        Booking booking = sessionUtil.getBooking(se.getSession());
        if (booking != null) {
            if (booking.getId() != null){
                //get latest booking from DB
                booking = bookingBaseDAO.findById(booking.getId());
            }
            cancelBooking(booking, now);
        }

        //also look for other blocking bookings that might no have been deleted
        LocalDateTime maxAge = now.minusSeconds(se.getSession().getMaxInactiveInterval());
        List<Booking> findBlockedBookings = bookingBaseDAO.findUnpaidBlockingBookings();
        for (Booking blockingBooking : findBlockedBookings) {
            cancelBooking(blockingBooking, maxAge);
        }
    }

    public static int getActiveSessions() {
        return activeSessions;
    }

    private void cancelBooking(Booking booking, LocalDateTime maxAge) {
        try {
            //if the booking is not confirmed and has not been blocked or is older than max Age
            if (!booking.getConfirmed() && (booking.getBlockingTime() == null || booking.getBlockingTime().isBefore(maxAge))) {
                switch (booking.getPaymentMethod()) {
                    case PayPal:
                        if (StringUtils.isEmpty(booking.getPaypalPaymentId())) {
                            LOG.info("Cancelling paypal booking [UUID:" + booking.getUUID() + ", user=" + booking.getPlayer().toString() + ", date=" + booking.getBookingDate() + ", time=" + booking.getBookingTime() + ", payment state=unpaid] due to session timeout");
                            bookingBaseDAO.cancelBooking(booking);
                        } else {
                            Payment payment = Payment.get(getApiContext(booking), booking.getPaypalPaymentId());
                            if (payment.getState() == null || !payment.getState().equals("approved")) {
                                LOG.info("Cancelling paypal booking [UUID:" + booking.getUUID() + ", user=" + booking.getPlayer().toString() + ", date=" + booking.getBookingDate() + ", time=" + booking.getBookingTime() + ", payment state=" + payment.getState() + "] due to session timeout");
                                bookingBaseDAO.cancelBooking(booking);
                            } else {
                                LOG.info("Fixing paypal booking [UUID:" + booking.getUUID() + ", user=" + booking.getPlayer().toString() + ", date=" + booking.getBookingDate() + ", time=" + booking.getBookingTime() + ", PayPal Payment ID:" + booking.getPaypalPaymentId() + "] that is approved by paypal but is not confirmed as paid, most likely to failed redirect from paypal to our system after successful payment");
                                booking.setConfirmed(Boolean.TRUE);
                                booking.setPaymentConfirmed(Boolean.TRUE);
                                bookingBaseDAO.saveOrUpdate(booking);
                            }
                        }
                        break;
                    default:
                        LOG.info("Cancelling booking [user=" + booking.getPlayer().toString() + ", date=" + booking.getBookingDate() + ", time=" + booking.getBookingTime() + ", paymentMethod=" + booking.getPaymentMethod() + "] due to session timeout");
                        bookingBaseDAO.cancelBooking(booking);
                }
            }
        } catch (Exception e) {
            LOG.error(e, e);
        }

    }

    private void initDependencies(ServletContext servletContext) {
        if (sessionUtil == null) {
            WebApplicationContextUtils.getWebApplicationContext(servletContext).getAutowireCapableBeanFactory().autowireBean(this);
        }
    }

    private APIContext getApiContext(Booking booking) throws Exception {
        PayPalConfig config = payPalConfigBaseDAO.findByCustomer(booking.getCustomer());
        if (config == null) {
            throw new Exception("No PayPal config found");
        }
        APIContext apiContext = new APIContext(config.getClientId(), config.getClientSecret(), config.getPayPalEndpoint().getMode());
        return apiContext;
    }
}
