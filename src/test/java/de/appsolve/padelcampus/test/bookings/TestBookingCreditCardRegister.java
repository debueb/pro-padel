/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test.bookings;

import de.appsolve.padelcampus.constants.BookingType;
import static de.appsolve.padelcampus.constants.Constants.SESSION_BOOKING;
import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.test.TestBase;
import org.joda.time.LocalDate;
import org.junit.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.util.Assert;

/**
 *
 * @author dominik
 */
public class TestBookingCreditCardRegister extends TestBase {

    @Test
    public void testBookingWorkflowCreditCardRegister() throws Exception {
        log.info("Test booking workflow [paymentMethod: CreditCard, bookingType: register]");
        LocalDate nextMonday = getNextMonday();
        
        mockMvc.perform(post("/bookings/" + nextMonday + "/10:00/offer/"+offer1.getId())
                .session(session)
                .param("bookingDate", nextMonday.toString())
                .param("bookingTime", "10:00")
                .param("offer", offer1.getId().toString())
                .param("bookingType", BookingType.register.name())
                .param("duration", "60")
                .param("paymentMethod", PaymentMethod.CreditCard.name()))
            .andExpect(redirectedUrl("/login/register"));
        
        Booking booking = (Booking) session.getAttribute(SESSION_BOOKING);
        Assert.notNull(booking);
        
        mockMvc.perform(post("/login/register")
                .session(session)
                .param("firstName", "test")
                .param("lastName", "bucher")
                .param("email", "padelcampus-unittest-3@appsolve.de")
                .param("phone", "01739398758")
                .param("password", "test"))
            .andExpect(redirectedUrl("/bookings/"+nextMonday+"/10:00/confirm"));
        
        mockMvc.perform(get("/bookings/"+nextMonday+"/10:00/confirm")
                .session(session))
            .andExpect(view().name("bookings/confirm"));
        
        booking = (Booking) session.getAttribute(SESSION_BOOKING);
        Assert.notNull(booking.getPlayer());
        
        mockMvc.perform(post("/bookings/"+nextMonday+"/10:00/confirm")
                .session(session)
                .param("accept-cancellation-policy", "on"))
            .andExpect(redirectedUrl("/bookings/booking/"+booking.getUUID()+"/creditcard"));
    }
}
