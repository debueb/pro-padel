package de.appsolve.padelcampus.controller.bookings;

import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.BookingMonitorDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.BookingMonitor;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import de.appsolve.padelcampus.utils.SessionUtil;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/bookings/monitor")
public class BookingsMonitorController {

    @Autowired
    SessionUtil sessionUtil;

    @Autowired
    BookingDAOI bookingDAO;

    @Autowired
    BookingMonitorDAOI bookingMonitorDAO;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
    }

    @RequestMapping(method = GET, value = "/{bookingUUID}")
    public ModelAndView getWatchStatus(HttpServletRequest request, @PathVariable String bookingUUID) {
        Player user = sessionUtil.getUser(request);
        if (user == null) {
            return getLoginView();

        }
        Booking booking = bookingDAO.findByUUID(bookingUUID);
        if (booking.getPlayer() != null && booking.getPlayer().equals(user)) {
            return new ModelAndView("bookings/monitor/cancel", "Booking", booking);
        }
        List<BookingMonitor> existingMonitors = bookingMonitorDAO.findByPlayerAndDateAndTime(user, booking.getBookingDate(), booking.getBookingTime());
        if (existingMonitors == null || existingMonitors.isEmpty()) {
            ModelAndView mav = new ModelAndView("bookings/monitor/notify");
            mav.addObject("Booking", booking);
            return mav;
        }
        return getWatchingView(booking);
    }

    @RequestMapping(method = POST, value = "/{bookingUUID}/watch")
    public ModelAndView watch(HttpServletRequest request, @PathVariable String bookingUUID) {
        Player user = sessionUtil.getUser(request);
        if (user == null) {
            return getLoginView();
        }
        Booking booking = bookingDAO.findByUUID(bookingUUID);
        BookingMonitor monitor = new BookingMonitor();
        monitor.setPlayer(user);
        monitor.setBookingDate(booking.getBookingDate());
        monitor.setBookingTime(booking.getBookingTime());
        bookingMonitorDAO.saveOrUpdate(monitor);
        return getWatchingView(booking);
    }

    private ModelAndView getLoginView() {
        return new ModelAndView("bookings/monitor/login");
    }

    private ModelAndView getWatchingView(Booking booking) {
        return new ModelAndView("bookings/monitor/watching", "Booking", booking);
    }
}
