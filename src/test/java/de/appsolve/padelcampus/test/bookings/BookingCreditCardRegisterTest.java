/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test.bookings;

import de.appsolve.padelcampus.constants.BookingType;
import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.test.TestBase;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.util.Assert;

import static de.appsolve.padelcampus.constants.Constants.SESSION_BOOKING;
import static de.appsolve.padelcampus.constants.Constants.SESSION_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author dominik
 */
public class BookingCreditCardRegisterTest extends TestBase {

    @Test
    public void testBookingWorkflowCreditCardRegister() throws Exception {
        LOG.info("Test booking workflow [paymentMethod: CreditCard, bookingType: register]");
        LocalDate nextMonday = getNextMonday();

        mockMvc.perform(post("/bookings/" + nextMonday + "/10:00/offer/" + offer1.getId())
                .session(session)
                .param("bookingTime", "10:00")
                .param("offer", offer1.getId().toString())
                .param("bookingType", BookingType.register.name())
                .param("duration", "60")
                .param("paymentMethod", PaymentMethod.CreditCard.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/login/register"));

        Booking booking = (Booking) session.getAttribute(SESSION_BOOKING);
        Assert.notNull(booking);

        mockMvc.perform(post("/login/register")
                .session(session)
                .param("firstName", "test")
                .param("lastName", "bucher")
                .param("email", "padelcampus-unittest-3@pro-padel.de")
                .param("phone", "01739398758")
                .param("password", "test"))
                .andExpect(redirectedUrl("/bookings/" + nextMonday + "/10:00/offer/" + offer1.getId()));

        Player user = (Player) session.getAttribute(SESSION_USER);
        Assert.notNull(user, "new registered user should be logged in");
        Assert.isTrue(!user.getGuest(), "new registered user should not be a guest");

        mockMvc.perform(post("/bookings/" + nextMonday + "/10:00/offer/" + offer1.getId())
                .session(session)
                .param("bookingTime", "10:00")
                .param("offer", offer1.getId().toString())
                .param("bookingType", BookingType.loggedIn.name())
                .param("duration", "60")
                .param("paymentMethod", PaymentMethod.CreditCard.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/bookings/" + nextMonday + "/10:00/confirm"));

        mockMvc.perform(get("/bookings/" + nextMonday + "/10:00/confirm")
                .session(session))
                .andExpect(view().name("bookings/confirm"))
                .andExpect(model().hasNoErrors());

        booking = (Booking) session.getAttribute(SESSION_BOOKING);
        Assert.notNull(booking.getPlayer());

        mockMvc.perform(post("/bookings/" + nextMonday + "/10:00/confirm")
                .session(session)
                .param("accept-cancellation-policy", "on"))
                .andExpect(redirectedUrl("/bookings/booking/" + booking.getUUID() + "/creditcard"));
    }
}
