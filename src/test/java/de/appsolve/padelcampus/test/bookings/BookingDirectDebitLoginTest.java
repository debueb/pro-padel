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
public class BookingDirectDebitLoginTest extends TestBase {

    @Test
    public void testBookingWorkflowDirectDebitLogin() throws Exception {
        LOG.info("Test booking workflow [paymentMethod: DirectDebit, bookingType: login]");
        LocalDate nextMonday = getNextMonday();

        Player player = new Player();
        player.setEmail("padelcampus-unittest-2@pro-padel.de");
        player.setFirstName("dummy");
        player.setLastName("dummy lastname");
        player.setPassword("test");
        player.setPhone("004917497568349");
        playerDAO.saveOrUpdate(player);

        mockMvc.perform(post("/bookings/" + nextMonday + "/10:00/offer/" + offer1.getId())
                .session(session)
                .param("bookingTime", "10:00")
                .param("offer", offer1.getId().toString())
                .param("bookingType", BookingType.login.name())
                .param("duration", "60")
                .param("paymentMethod", PaymentMethod.DirectDebit.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/login"));

        Booking booking = (Booking) session.getAttribute(SESSION_BOOKING);
        Assert.notNull(booking);

        mockMvc.perform(post("/login")
                .session(session)
                .param("email", "padelcampus-unittest-2@pro-padel.de")
                .param("password", "test"))
                .andExpect(redirectedUrl("/bookings/" + nextMonday + "/10:00/offer/" + offer1.getId()));

        Player user = (Player) session.getAttribute(SESSION_USER);
        Assert.notNull(user, "user should be logged in");

        mockMvc.perform(post("/bookings/" + nextMonday + "/10:00/offer/" + offer1.getId())
                .session(session)
                .param("bookingTime", "10:00")
                .param("offer", offer1.getId().toString())
                .param("bookingType", BookingType.loggedIn.name())
                .param("duration", "60")
                .param("paymentMethod", PaymentMethod.DirectDebit.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/bookings/" + nextMonday + "/10:00/confirm"));

        mockMvc.perform(get("/bookings/" + nextMonday + "/10:00/confirm")
                .session(session))
                .andExpect(view().name("bookings/confirm"));

        booking = (Booking) session.getAttribute(SESSION_BOOKING);
        Assert.notNull(booking, "Booking should be in session");
        Assert.notNull(booking.getPlayer(), "Booking should have a player");

        mockMvc.perform(post("/bookings/" + nextMonday + "/10:00/confirm")
                .session(session)
                .param("accept-cancellation-policy", "on"))
                .andExpect(redirectedUrl("/bookings/booking/" + booking.getUUID() + "/directdebit"));
    }
}
