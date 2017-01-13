/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test.bookings;

import de.appsolve.padelcampus.constants.BookingType;
import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.test.TestBase;
import org.joda.time.LocalDate;
import org.junit.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author dominik
 */
public class TestBookingInvalidUserSelection extends TestBase {
    
    @Test
    public void testBookingInvalidDate() throws Exception {
        LOG.info("Test booking workflow with invalid date [paymentMethod: CreditCard, bookingType: register]");
        LocalDate prevMonday = getLastMonday();
        
        mockMvc.perform(post("/bookings/" + prevMonday + "/10:00/offer/"+offer1.getId())
                .session(session)
                .param("bookingTime", "10:00")
                .param("offer", offer1.getId().toString())
                .param("bookingType", BookingType.register.name())
                .param("duration", "60")
                .param("paymentMethod", PaymentMethod.CreditCard.name()))
            .andExpect(status().is2xxSuccessful())
            .andExpect(model().attributeExists("error"))
            .andExpect(model().attribute("error", msg.get("RequestedTimeIsInThePast")));
    }
    
    @Test
    public void testBookingInvalidTime() throws Exception {
        LOG.info("Test booking workflow with invalid date [paymentMethod: CreditCard, bookingType: register]");
        LocalDate nextMonday = getNextMonday();
        
        mockMvc.perform(post("/bookings/" + nextMonday + "/10:11/offer/"+offer1.getId())
                .session(session)
                .param("bookingTime", "10:00")
                .param("offer", offer1.getId().toString())
                .param("bookingType", BookingType.register.name())
                .param("duration", "60")
                .param("paymentMethod", PaymentMethod.CreditCard.name()))
            .andExpect(status().is2xxSuccessful())
            .andExpect(model().attributeExists("error"))
            .andExpect(model().attribute("error", msg.get("NoFreeCourtsForSelectedTimeAndDate")));
    }
}
