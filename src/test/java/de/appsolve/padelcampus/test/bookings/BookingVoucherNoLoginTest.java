/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test.bookings;

import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.Player;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dominik
 */
public class BookingVoucherNoLoginTest extends BookingVoucherTest {


    @Test
    public void testBookingWorkflowVoucherNoLogin() throws Exception {
        bookViaVoucherAndNoLogin(offer1);
        bookViaVoucherAndNoLogin(offer2);

        //make sure user does not get created multiple times
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("email", "padelcampus-unittest-1@pro-padel.de");
        List<Player> players = playerDAO.findByAttributes(attrs);
        Assert.notNull(players, "player should exist");
        Assert.isTrue(players.size() == 1, "player should only exist once");

        List<Booking> activeBookings = bookingDAO.findActiveBookingsBetween(getNextMonday(), getNextMonday());
        Assert.notNull(activeBookings, "bookings should exist");
        Assert.isTrue(activeBookings.size() == 2, "2 bookings should exist");
    }
}
