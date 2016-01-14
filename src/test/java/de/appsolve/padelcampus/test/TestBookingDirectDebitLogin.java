/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test;

import de.appsolve.padelcampus.constants.BookingType;
import static de.appsolve.padelcampus.constants.Constants.SESSION_BOOKING;
import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.Player;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.util.Assert;

/**
 *
 * @author dominik
 */
public class TestBookingDirectDebitLogin extends TestBase {

    @Autowired
    private PlayerDAOI playerDAO;

    @Test
    public void testBookingWorkflowDirectDebitLogin() throws Exception {
        log.info("Test booking workflow [paymentMethod: DirectDebit, bookingType: login]");
        LocalDate nextMonday = getNextMonday();
        
        Player player = new Player();
        player.setEmail("padelcampus-unittest-2@appsolve.de");
        player.setPasswordHash(DigestUtils.sha512Hex("test"));
        playerDAO.saveOrUpdate(player);
        
        mockMvc.perform(post("/bookings/" + nextMonday + "/10:00/offer/"+offer1.getId())
                .session(session)
                .param("bookingDate", nextMonday.toString())
                .param("bookingTime", "10:00")
                .param("offer", offer1.getId().toString())
                .param("bookingType", BookingType.login.name())
                .param("duration", "60")
                .param("paymentMethod", PaymentMethod.DirectDebit.name()))
            .andExpect(redirectedUrl("/login"));
        
        Booking booking = (Booking) session.getAttribute(SESSION_BOOKING);
        Assert.notNull(booking);
        
        mockMvc.perform(post("/login")
                .session(session)
                .param("email", "padelcampus-unittest-2@appsolve.de")
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
            .andExpect(redirectedUrl("/bookings/booking/"+booking.getUUID()+"/directdebit"));
    }
}
