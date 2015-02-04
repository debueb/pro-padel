/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test;

import org.apache.log4j.Logger;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasSize;
import org.joda.time.LocalDate;
import org.junit.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author dominik
 */
public class TestCoreFunctions extends TestBase {

    private static final Logger log = Logger.getLogger(TestCoreFunctions.class);

    @Test
    public void testRootPage() throws Exception {
        log.info("Make sure root page contains news");
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("AllNews"));
    }

    @Test
    public void testBookingsIndexPage() throws Exception {
        log.info("Make sure bookings index page contains time slots if a matching calendar config exists");
        LocalDate nextMonday = getNextMonday();
        mockMvc.perform(get("/bookings/" + nextMonday))
                .andExpect(status().isOk())
                .andExpect(view().name("bookings/index"))
                .andExpect(model().attribute("RangeMap", not(hasSize(0))));
    }

    @Test
    public void testBookingsDetailTimeUnavaiblablePage() throws Exception {
        log.info("Make sure bookings detail page shows error if no time slot exists");
        LocalDate nextMonday = getNextMonday();
        mockMvc.perform(get("/bookings/" + nextMonday + "/09:00"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookings/booking"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void testBookingsDetailDateUnavaiblablePage() throws Exception {
        log.info("Make sure bookings detail page shows error if no matching calendar config exists");
        LocalDate nextMonday = getNextMonday();
        LocalDate nextTuesday = nextMonday.plusDays(1);
        mockMvc.perform(get("/bookings/" + nextTuesday + "/10:00"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookings/booking"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void testBookingsDetailPageHasDurations() throws Exception {
        log.info("Make sure bookings detail page contains duration when matching calendar config exists");
        LocalDate nextMonday = getNextMonday();
        mockMvc.perform(get("/bookings/" + nextMonday + "/10:00"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookings/booking"))
                .andExpect(model().attribute("OfferDurationPrices", not(hasSize(0))));
    }
}
