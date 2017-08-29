/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test.bookings;

import de.appsolve.padelcampus.constants.BookingType;
import de.appsolve.padelcampus.constants.PaymentMethod;
import org.joda.time.LocalDate;
import org.junit.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author dominik
 */
public class BookingOverbookingTest extends BookingVoucherTest {
    
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
                .param("bookingTime", "10:00")
                .param("offer", offer1.getId().toString())
                .param("bookingType", BookingType.nologin.name())
                .param("duration", "60")
                .param("paymentMethod", PaymentMethod.Voucher.name()))
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name("bookings/booking"))
            .andExpect(model().attribute("error", msg.get("NoFreeCourtsForSelectedTimeAndDate")));
        
        mockMvc.perform(post("/bookings/"+nextMonday+"/10:00/confirm")
                .session(session)
                .param("accept-cancellation-policy", "on"))
            .andExpect(redirectedUrl("/bookings/" + nextMonday + "/10:00/offer/"+offer2.getId()))
            .andExpect(flash().attribute("error", msg.get("NoFreeCourtsForSelectedTimeAndDate")));
    }
}
