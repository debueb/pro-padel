/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test;

import org.joda.time.LocalDate;
import org.junit.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasSize;

/**
 *
 * @author dominik
 */
public class TestCoreFunctions extends TestBase {

    @Test
    public void testRootPage() throws Exception {
        LOG.info("Make sure root page contains news");
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("Mail"));
    }

    @Test
    public void testBookingsIndexPage() throws Exception {
        LOG.info("Make sure bookings index page contains time slots if a matching calendar config exists");
        LocalDate nextMonday = getNextMonday();
        mockMvc.perform(get("/bookings/?date=" + nextMonday))
                .andExpect(status().isOk())
                .andExpect(view().name("bookings/index"))
                .andExpect(model().attribute("RangeMap", not(hasSize(0))));
    }

    @Test
    public void testBookingsDetailsPageHasDurations() throws Exception {
        LOG.info("Make sure bookings detail page contains duration when matching calendar config exists");
        LocalDate nextMonday = getNextMonday();
        mockMvc.perform(get("/bookings/" + nextMonday + "/10:00/offer/"+offer1.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("bookings/booking"))
                .andExpect(model().attribute("OfferDurationPrices", not(hasSize(0))));
    }
}
