package de.appsolve.padelcampus.admin.controller.reports;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.DateRange;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.Valid;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/reports")
public class AdminReportsController extends BaseController{
    
    @Autowired
    BookingDAOI bookingDAO;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
    }
    
    @RequestMapping()
    public ModelAndView getIndex(){
        return new ModelAndView("admin/reports/index");
    }
    
    @RequestMapping("bookinglist")
    public ModelAndView getBookingList(){
        LocalDate endDate = new LocalDate();
        LocalDate startDate = endDate.minusMonths(3);
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(startDate);
        dateRange.setEndDate(endDate);
        return getBookingListView(dateRange);
    }
    
    @RequestMapping("bookinglist/{date}")
    public ModelAndView getBookingList(@PathVariable("date") String date){
        LocalDate startDate = new LocalDate(date);
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(startDate);
        dateRange.setEndDate(startDate);
        return getBookingListView(dateRange);
    }
    
    @RequestMapping(value="bookinglist", method=POST)
    public ModelAndView getBookingListForDateRange(@Valid @ModelAttribute("DateRange") DateRange dateRange){
        return getBookingListView(dateRange); 
    }

    private ModelAndView getBookingListView(DateRange dateRange) {
        List<Booking> bookings = bookingDAO.findActiveBookingsBetween(dateRange.getStartDate(), dateRange.getEndDate());
        
        BigDecimal total = new BigDecimal(0);
        for (Booking booking: bookings){
            total = total.add(booking.getAmount());
        }
        ModelAndView listView = new ModelAndView("admin/reports/bookinglist");
        listView.addObject("Total", total);
        listView.addObject("Bookings", bookings);
        listView.addObject("DateRange", dateRange);
        return listView;
    }
}
