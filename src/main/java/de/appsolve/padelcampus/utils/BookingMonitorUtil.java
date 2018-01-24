package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.dao.BookingMonitorDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.BookingMonitor;
import de.appsolve.padelcampus.reporting.ErrorReporter;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class BookingMonitorUtil {

    private static final Logger LOG = Logger.getLogger(BookingMonitorUtil.class);

    @Autowired
    BookingMonitorDAOI bookingMonitorDAO;

    @Autowired
    MailUtils mailUtils;

    @Autowired
    Msg msg;

    @Autowired
    ErrorReporter errorReporter;

    public void notifyUsers(HttpServletRequest request, Booking booking) {
        try {
            String baseURL = RequestUtil.getBaseURL(request);
            LocalDate date = booking.getBookingDate();
            LocalTime time = booking.getBookingTime();
            String dateStr = FormatUtils.DATE_MEDIUM.print(date);
            String timeStr = FormatUtils.TIME_HUMAN_READABLE.print(time);
            if (date.isAfter(LocalDate.now()) || date.equals(LocalDate.now()) && time.isAfter(LocalTime.now())) {
                List<BookingMonitor> bookingMonitors = bookingMonitorDAO.findByDateAndTime(date, time);
                if (bookingMonitors != null) {
                    for (BookingMonitor monitor : bookingMonitors) {
                        String subject = msg.get("BookingMonitorAvailableMailSubject", new Object[]{booking.getOffer(), dateStr, timeStr});
                        String body = msg.get("BookingMonitorAvailableMailBody", new Object[]{monitor.getPlayer(), booking.getOffer(), dateStr, timeStr, baseURL + "/bookings", baseURL});
                        Mail mail = new Mail();
                        mail.addRecipient(monitor.getPlayer());
                        mail.setSubject(subject);
                        mail.setBody(body);
                        mailUtils.send(mail, request);
                    }
                }
            } else {
                LOG.warn(String.format("Not notifying any users about new booking opportunity because %s %s has passed", dateStr, timeStr));
            }
        } catch (Exception e) {
            errorReporter.notify(e);
        }
    }
}
