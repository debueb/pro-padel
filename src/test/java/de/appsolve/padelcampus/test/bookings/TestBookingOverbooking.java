/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test.bookings;

import de.appsolve.padelcampus.constants.BookingType;
import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.utils.Msg;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author dominik
 */
public class TestBookingOverbooking extends TestBookingVoucher {
    
    @Autowired
    Msg msg;
    
    @Test
    public void testBookingOverbooking() throws Exception {
        LOG.info("Test booking overbooking");
               
        bookViaVoucherAndNoLogin(offer1);
        bookViaVoucherAndNoLogin(offer2);
        
        LocalDate nextMonday = getNextMonday();
        mockMvc.perform(get("/bookings/" + nextMonday + "/10:00/offer/"+offer1.getId())
                .session(session))
            .andExpect(view().name("bookings/booking"))
            .andExpect(model().attribute("error", msg.get("NoFreeCourtsForSelectedTimeAndDate")));
        
        mockMvc.perform(post("/bookings/" + nextMonday + "/10:00/offer/"+offer1.getId())
                .session(session)
                .param("bookingDate", nextMonday.toString())
                .param("bookingTime", "10:00")
                .param("offer", offer1.getId().toString())
                .param("bookingType", BookingType.nologin.name())
                .param("duration", "60")
                .param("paymentMethod", PaymentMethod.Voucher.name()))
            .andExpect(view().name("bookings/booking"))
            .andExpect(model().attribute("error", msg.get("NoFreeCourtsForSelectedTimeAndDate")));
        
        //fake booking
        Booking booking = new Booking();
        booking.setBookingDate(nextMonday);
        booking.setBookingTime(new LocalTime("10:00"));
        booking.setBookingType(BookingType.nologin);
        booking.setDuration(60L);
        
        mockMvc.perform(post("/bookings/"+nextMonday+"/10:00/confirm")
                .session(session)
                .param("accept-cancellation-policy", "on"))
            .andExpect(model().attribute("error", msg.get("NoFreeCourtsForSelectedTimeAndDate")));
        
//        mockMvc.perform(post("/bookings/booking/"+booking.getUUID()+"/voucher")
//                .session(session)
//                .param("voucherUUID", testVoucher.getUUID()))
//            .andExpect(redirectedUrl("/bookings/booking/" + booking.getUUID() + "/success"));
    }
}
