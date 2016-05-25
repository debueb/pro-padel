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
import de.appsolve.padelcampus.db.dao.FacilityDAOI;
import de.appsolve.padelcampus.utils.BookingUtil;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    FacilityDAOI facilityDAO;
    
    @Autowired
    CalendarConfigDAOI calendarConfigDAO;
    
    @Autowired
    BookingUtil bookingUtil;
    
    @RequestMapping()
    public ModelAndView getBookings(@RequestParam(value="date", required=false) String date) throws JsonProcessingException{
        LocalDate localDate;
        if (date == null){
            localDate = new LocalDate();
        } else {
            localDate = DATE_HUMAN_READABLE.parseLocalDate(date);
        }
        return getBookingsView(localDate);
    }
    
    private ModelAndView getBookingsView(LocalDate date) throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("admin/reports/allocations/index");
        bookingUtil.addWeekView(date, facilityDAO.findAll(), mav, false);
        return mav;
    }
}
