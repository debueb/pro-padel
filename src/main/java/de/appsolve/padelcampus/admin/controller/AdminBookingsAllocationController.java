/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.TimeSlot;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.CalendarConfigDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.utils.BookingUtil;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE;
import java.util.List;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/bookings/allocations")
public class AdminBookingsAllocationController extends BaseController{

    @Autowired
    BookingDAOI bookingDAO;
    
    @Autowired
    CalendarConfigDAOI calendarConfigDAO;
    
    @Autowired
    BookingUtil bookingUtil;
    
    @RequestMapping()
    public ModelAndView getBookings(){
        return getBookingsView(new LocalDate());
    }
    
    @RequestMapping(value = "{date}")
    public ModelAndView getBookingsForDate(@PathVariable("date") String date){
        LocalDate selectedDate = DATE_HUMAN_READABLE.parseLocalDate(date);
        return getBookingsView(selectedDate);
    }
   
    private ModelAndView getBookingsView(LocalDate date) {
        ModelAndView mav = new ModelAndView("admin/bookings/allocations/index");
        mav.addObject("Day", date);
        try {
            List<CalendarConfig> calendarConfigs = calendarConfigDAO.findFor(date);
            List<TimeSlot> timeSlots = bookingUtil.getTimeSlotsForDateAndCalendarConfigs(date, calendarConfigs, false);
            mav.addObject("TimeSlots", timeSlots);
            int courtCount = 0;
            for (CalendarConfig config : calendarConfigs) {
                courtCount = Math.max(courtCount, config.getCourtCount());
            }
            mav.addObject("CourtCount", courtCount);
        } catch (Exception e){
            mav.addObject("error", e.getMessage());
        }
        return mav;
    }
}
