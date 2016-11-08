package de.appsolve.padelcampus.admin.controller.reports;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.DateRange;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.MasterDataDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.MasterData;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/reports")
public class AdminReportsController extends BaseController{
    
    private static final Logger LOG = Logger.getLogger(AdminReportsController.class);
    
    @Autowired
    BookingDAOI bookingDAO;
    
    @Autowired
    SessionUtil sessionUtil;
    
    @Autowired
    MasterDataDAOI masterDataDAO;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
    }
    
    @RequestMapping()
    public ModelAndView getIndex(){
        return new ModelAndView("admin/reports/index");
    }
    
    @RequestMapping("bookinglist")
    public ModelAndView getBookingList(HttpServletRequest request){
        LocalDate startDate = sessionUtil.getBookingListStartDate(request);
        if (startDate == null){
            startDate = new LocalDate();
        }
        LocalDate endDate = sessionUtil.getBookingListEndDate(request);
        if (endDate == null){
            endDate = startDate;
        }
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
    
    @RequestMapping(value={"bookinglist", "bookinglist/{date}"}, method=POST)
    public ModelAndView getBookingListForDateRange(HttpServletRequest request, @Valid @ModelAttribute("DateRange") DateRange dateRange){
        sessionUtil.setBookingListStartDate(request, dateRange.getStartDate());
        if (dateRange.getEndDate().isBefore(dateRange.getStartDate())){
            dateRange.setEndDate(dateRange.getStartDate());
        }
        sessionUtil.setBookingListEndDate(request, dateRange.getEndDate());
        return getBookingListView(dateRange); 
    }
    
    @RequestMapping("bookinglist/print/{start}/{end}")
    public ModelAndView getPrintInvoices(@PathVariable("start") String start, @PathVariable("end") String end){
        MasterData masterData = masterDataDAO.findFirst();
        if (masterData == null){
            return new ModelAndView("invoices/masterdata_missing");
        }
        LocalDate startDate = new LocalDate(start);
        LocalDate endDate = new LocalDate(end);
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(startDate);
        dateRange.setEndDate(endDate);
        List<Booking> bookings = bookingDAO.findActiveBookingsBetween(dateRange.getStartDate(), dateRange.getEndDate());
        ModelAndView mav = new ModelAndView("admin/reports/printinvoices");
        mav.addObject("MasterData", masterData);
        mav.addObject("Bookings", bookings);
        return mav;
    }
    
    @RequestMapping(method = GET, value="booking/{bookingId}")
    public ModelAndView getBooking(@PathVariable("bookingId") Long bookingId){
        Booking booking = bookingDAO.findById(bookingId);
        return getBookingEditView(booking);
    }
    
    @RequestMapping(method = POST, value="booking/{bookingId}")
    public ModelAndView postBooking(@PathVariable("bookingId") Long bookingId, @Valid @ModelAttribute("Model") Booking model, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return getBookingEditView(model);
        }
        Booking booking = bookingDAO.findById(bookingId);
        try {
            booking.setComment(model.getComment());
            booking.setPaymentConfirmed(model.getPaymentConfirmed());
            bookingDAO.saveOrUpdate(booking);
            return redirectToBookingList();
        }catch (Exception e){
            LOG.error(e);
            bindingResult.addError(new ObjectError("id", e.getMessage()));
            return getBookingEditView(booking);
        }
    }
    
    @RequestMapping(method = GET, value="booking/{bookingId}/delete")
    public ModelAndView getBookingDelete(@PathVariable("bookingId") Long bookingId){
        Booking booking = bookingDAO.findById(bookingId);
        return getBookingDeleteView(booking);
    }
    
    @RequestMapping(method = POST, value="booking/{bookingId}/delete")
    public ModelAndView postBookingDelete(@PathVariable("bookingId") Long bookingId){
        bookingDAO.deleteById(bookingId);
        return redirectToBookingList();
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

    private ModelAndView getBookingEditView(Booking booking) {
        ModelAndView mav = new ModelAndView("admin/reports/booking");
        mav.addObject("Booking", booking);
        return mav;
    }
    
    private ModelAndView getBookingDeleteView(Booking booking) {
        ModelAndView mav = new ModelAndView("include/delete");
        mav.addObject("Model", booking);
        return mav;
    }

    private ModelAndView redirectToBookingList() {
        return new ModelAndView("redirect:/admin/reports/bookinglist");
    }
}
