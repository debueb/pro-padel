/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test.bookings;

import de.appsolve.padelcampus.constants.BookingType;
import de.appsolve.padelcampus.constants.CalendarWeekDay;
import static de.appsolve.padelcampus.constants.Constants.SESSION_BOOKING;
import static de.appsolve.padelcampus.constants.Constants.BOOKING_DEFAULT_VALID_FROM_HOUR;
import static de.appsolve.padelcampus.constants.Constants.BOOKING_DEFAULT_VALID_FROM_MINUTE;
import static de.appsolve.padelcampus.constants.Constants.BOOKING_DEFAULT_VALID_UNTIL_HOUR;
import static de.appsolve.padelcampus.constants.Constants.BOOKING_DEFAULT_VALID_UNTIL_MINUTE;
import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.db.dao.VoucherDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.Offer;
import de.appsolve.padelcampus.db.model.Voucher;
import de.appsolve.padelcampus.test.TestBase;
import static de.appsolve.padelcampus.utils.FormatUtils.TIME_HUMAN_READABLE;
import de.appsolve.padelcampus.utils.VoucherUtil;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import org.springframework.util.Assert;

/**
 *
 * @author dominik
 */
public abstract class TestBookingVoucher extends TestBase{
    
    @Autowired
    private VoucherDAOI voucherDAO;
    
    protected void bookViaVoucherAndNoLogin(Offer offer) throws Exception {
        log.info("Test booking workflow [paymentMethod: Voucher, bookingType: nologin]");
        LocalDate nextMonday = getNextMonday();
        LocalTime validFromTime = TIME_HUMAN_READABLE.parseLocalTime(BOOKING_DEFAULT_VALID_FROM_HOUR+":"+BOOKING_DEFAULT_VALID_FROM_MINUTE);
        LocalTime validUntilTime = TIME_HUMAN_READABLE.parseLocalTime(BOOKING_DEFAULT_VALID_UNTIL_HOUR+":"+BOOKING_DEFAULT_VALID_UNTIL_MINUTE);
        Voucher testVoucher = VoucherUtil.createNewVoucher("test voucher", 60L, getNextMonday(), validFromTime, validUntilTime, CalendarWeekDay.valuesAsSet(), offers);
        voucherDAO.saveOrUpdate(testVoucher);

        mockMvc.perform(post("/bookings/" + nextMonday + "/10:00/offer/"+offer.getId())
                .session(session)
                .param("bookingDate", nextMonday.toString())
                .param("bookingTime", "10:00")
                .param("offer", offer.getId().toString())
                .param("bookingType", BookingType.nologin.name())
                .param("duration", "60")
                .param("paymentMethod", PaymentMethod.Voucher.name()))
            .andExpect(redirectedUrl("/bookings/nologin"));
        
        Booking booking = (Booking) session.getAttribute(SESSION_BOOKING);
        Assert.notNull(booking);
        
        mockMvc.perform(post("/bookings/nologin")
                .session(session)
                .param("firstName", "test")
                .param("lastName", "bucher")
                .param("email", "padelcampus-unittest-1@appsolve.de")
                .param("phone", "01739398758"))
            .andExpect(redirectedUrl("/bookings/"+nextMonday+"/10:00/confirm"));
        
        booking = (Booking) session.getAttribute(SESSION_BOOKING);
        Assert.notNull(booking.getPlayer());
        
        mockMvc.perform(post("/bookings/"+nextMonday+"/10:00/confirm")
                .session(session)
                .param("accept-cancellation-policy", "on"))
            .andExpect(redirectedUrl("/bookings/booking/"+booking.getUUID()+"/voucher"));
        
        mockMvc.perform(post("/bookings/booking/"+booking.getUUID()+"/voucher")
                .session(session)
                .param("voucherUUID", testVoucher.getUUID()))
            .andExpect(redirectedUrl("/bookings/booking/" + booking.getUUID() + "/success"));
    }
}
