/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.reports;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.CalendarConfigDAOI;
import de.appsolve.padelcampus.utils.BookingUtil;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE;
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
@RequestMapping("/admin/reports/allocations")
public class AdminReportsAllocationController extends BaseController{

    @Autowired
    BookingDAOI bookingDAO;
    
    @Autowired
    CalendarConfigDAOI calendarConfigDAO;
    
    @Autowired
    BookingUtil bookingUtil;
    
    @RequestMapping()
    public ModelAndView getBookings() throws JsonProcessingException{
        return getBookingsView(new LocalDate());
    }
    
    @RequestMapping(value = "{date}")
    public ModelAndView getBookingsForDate(@PathVariable("date") String date) throws JsonProcessingException{
        LocalDate selectedDate = DATE_HUMAN_READABLE.parseLocalDate(date);
        return getBookingsView(selectedDate);
    }
   
    private ModelAndView getBookingsView(LocalDate date) throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("admin/reports/allocations/index");
        bookingUtil.addWeekView(date, mav, false);
        return mav;
    }
}
