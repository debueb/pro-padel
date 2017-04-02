package de.appsolve.padelcampus.controller.bookings;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import de.appsolve.padelcampus.constants.CalendarWeekDay;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.VoucherDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.Voucher;
import static de.appsolve.padelcampus.utils.FormatUtils.TIME_HUMAN_READABLE;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/bookings")
public class BookingsVoucherController extends BookingsPaymentController{
    
    private static final Logger log = Logger.getLogger(BookingsVoucherController.class);
    
    @Autowired
    SessionUtil sessionUtil;
    
    @Autowired
    BookingDAOI bookingDAO;
    
    @Autowired
    VoucherDAOI voucherDAO;
    
    public ModelAndView redirectToVoucher(Booking booking) throws Exception {
        return new ModelAndView("redirect:/bookings/booking/"+booking.getUUID()+"/voucher");
    }
    
    @RequestMapping(value = "booking/{UUID}/voucher", method=GET)
    public ModelAndView onGetVoucher(@PathVariable("UUID") String UUID){
        Booking booking = bookingDAO.findByUUID(UUID);
        return getVoucherView(booking);
    }
    
    @RequestMapping(value = "booking/{UUID}/voucher", method=POST)
    public ModelAndView onPostVoucher(@PathVariable("UUID") String UUID, @RequestParam(required = false) String voucherUUID){
        Booking booking = bookingDAO.findByUUID(UUID);
        ModelAndView mav = getVoucherView(booking);
        try {
            if (booking.getConfirmed()){
                throw new Exception(msg.get("BookingAlreadyConfirmed"));
            }
            if (StringUtils.isEmpty(voucherUUID)){
                throw new Exception(msg.get("MissingRequiredVoucher"));
            }
            Voucher voucher = voucherDAO.findByUUID(voucherUUID);
            LocalDate now = new LocalDate();
          
            if (voucher == null){
                throw new Exception(msg.get("VoucherDoesNotExist"));
            } 
            
            if (voucher.getUsed()){
                throw new Exception(msg.get("VoucherHasAlreadyBeenUsed"));
            }
            
            if (now.isAfter(voucher.getValidUntil())){
                throw new Exception(msg.get("VoucherHasExpired"));
            }
            
            if (voucher.getDuration().compareTo(booking.getDuration()) < 0){
                throw new Exception(msg.get("VoucherIsOnlyValidForDuration", new Object[]{voucher.getDuration()}));
            }
            
            if (!voucher.getOffers().contains(booking.getOffer())){
                throw new Exception(msg.get("VoucherIsOnlyValidForOffer", new Object[]{voucher.getOffers()}));
            }

            LocalTime validUntilTime = voucher.getValidUntilTime();
            LocalTime bookingEndTime = booking.getBookingEndTime();
            if (bookingEndTime.isAfter(validUntilTime)){
                throw new Exception(msg.get("VoucherIsOnlyValidForBookingsBefore", new Object[]{validUntilTime.toString(TIME_HUMAN_READABLE)}));
            }

            //update legacy vouchers
            if (voucher.getCalendarWeekDays().isEmpty()){
                voucher.setCalendarWeekDays(new HashSet<>(Arrays.asList(CalendarWeekDay.values())));
            }
            
            Set<CalendarWeekDay> validWeekDays = voucher.getCalendarWeekDays();
            CalendarWeekDay bookingWeekDay = CalendarWeekDay.values()[booking.getBookingDate().getDayOfWeek()-1];
            if (!validWeekDays.contains(bookingWeekDay)){
                throw new Exception(msg.get("VoucherIsOnlyValidForBookingsOn", new Object[]{validWeekDays}));
            }
            
            voucher.setUsed(true);
            voucherDAO.saveOrUpdate(voucher);
            booking.setPaymentConfirmed(true);
            booking.setVoucher(voucher);
            bookingDAO.saveOrUpdate(booking);
            return BookingsController.getRedirectToSuccessView(booking);
        } catch (Exception e){
            log.error(e);
            mav.addObject("error", e.getMessage());
            return mav;
        }
    }

    private ModelAndView getVoucherView(Booking booking) {
        ModelAndView mav = new ModelAndView("bookings/voucher", "Booking", booking);
        return mav;
    }    
}