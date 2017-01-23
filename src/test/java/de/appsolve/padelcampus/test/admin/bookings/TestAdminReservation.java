/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test.admin.bookings;

import de.appsolve.padelcampus.constants.CalendarWeekDay;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.test.TestBase;
import java.util.List;
import org.joda.time.LocalTime;
import org.junit.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.util.Assert;

/**
 *
 * @author dominik
 */
public class TestAdminReservation extends TestBase{
    
    @Test
    public void testReservations() throws Exception{
        
        createAdminAccount();
        
        mockMvc.perform(get("/admin/bookings")
                .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        
        login(ADMIN_EMAIL, ADMIN_PASSWORD);
        
        mockMvc.perform(get("/admin/bookings/reservations")
                .session(session))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().hasNoErrors());
        
        mockMvc.perform(get("/admin/bookings/reservations/add")
                .session(session))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().hasNoErrors());
        
        LOG.info("reserving a court");
        mockMvc.perform(post("/admin/bookings/reservations/add")
                .session(session)
                .param("startDate", getNextMonday().toString())
                .param("startTimeHour", "10")
                .param("startTimeMinute", "00")
                .param("endDate", getNextMonday().toString())
                .param("endTimeHour", "11")
                .param("endTimeMinute", "30")
                .param("holidayKey", "NO_HOLIDAY")
                .param("paymentConfirmed", "true")
                .param("publicBooking", "true")
                .param("offers", ""+offer1.getId())
                .param("comment", "Testreservierung")
                .param("calendarWeekDays", CalendarWeekDay.Monday.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/admin/bookings/reservations"));
        
        LOG.info("reserving the same court twice should fail");
        mockMvc.perform(post("/admin/bookings/reservations/add")
                .session(session)
                .param("startDate", getNextMonday().toString())
                .param("startTimeHour", "10")
                .param("startTimeMinute", "00")
                .param("endDate", getNextMonday().toString())
                .param("endTimeHour", "11")
                .param("endTimeMinute", "30")
                .param("holidayKey", "NO_HOLIDAY")
                .param("paymentConfirmed", "true")
                .param("publicBooking", "true")
                .param("offers", ""+offer1.getId())
                .param("comment", "Testreservierung")
                .param("calendarWeekDays", CalendarWeekDay.Monday.toString()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().hasErrors());
        
        LOG.info("reserving overlapping courts should fail");
        mockMvc.perform(post("/admin/bookings/reservations/add")
                .session(session)
                .param("startDate", getNextMonday().toString())
                .param("startTimeHour", "11")
                .param("startTimeMinute", "00")
                .param("endDate", getNextMonday().toString())
                .param("endTimeHour", "12")
                .param("endTimeMinute", "00")
                .param("holidayKey", "NO_HOLIDAY")
                .param("paymentConfirmed", "true")
                .param("publicBooking", "true")
                .param("offers", ""+offer1.getId())
                .param("comment", "Testreservierung")
                .param("calendarWeekDays", CalendarWeekDay.Monday.toString()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().hasErrors());
        
        LOG.info("reserving a second court should work");
        mockMvc.perform(post("/admin/bookings/reservations/add")
                .session(session)
                .param("startDate", getNextMonday().toString())
                .param("startTimeHour", "10")
                .param("startTimeMinute", "00")
                .param("endDate", getNextMonday().toString())
                .param("endTimeHour", "11")
                .param("endTimeMinute", "30")
                .param("holidayKey", "NO_HOLIDAY")
                .param("paymentConfirmed", "true")
                .param("publicBooking", "true")
                .param("offers", ""+offer2.getId())
                .param("comment", "Testreservierung")
                .param("calendarWeekDays", CalendarWeekDay.Monday.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/admin/bookings/reservations"));
        
        LOG.info("modifying the first reservation in conflict with the second reservation should fail");
        mockMvc.perform(post("/admin/bookings/reservations/booking/1")
                .session(session)
                .param("startDate", getNextMonday().toString())
                .param("startTimeHour", "10")
                .param("startTimeMinute", "00")
                .param("endDate", getNextMonday().toString())
                .param("endTimeHour", "11")
                .param("endTimeMinute", "30")
                .param("holidayKey", "NO_HOLIDAY")
                .param("paymentConfirmed", "true")
                .param("publicBooking", "true")
                .param("offers", ""+offer2.getId())
                .param("comment", "Testreservierung")
                .param("calendarWeekDays", CalendarWeekDay.Monday.toString()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().hasErrors());
        
        LOG.info("modifying the first reservation to a later time should work");
        mockMvc.perform(post("/admin/bookings/reservations/booking/1")
                .session(session)
                .param("startDate", getNextMonday().toString())
                .param("startTimeHour", "11")
                .param("startTimeMinute", "00")
                .param("endDate", getNextMonday().toString())
                .param("endTimeHour", "12")
                .param("endTimeMinute", "30")
                .param("holidayKey", "NO_HOLIDAY")
                .param("paymentConfirmed", "true")
                .param("publicBooking", "true")
                .param("offers", ""+offer1.getId())
                .param("comment", "Testreservierung")
                .param("calendarWeekDays", CalendarWeekDay.Monday.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/bookings/reservations"));
        
        LOG.info("modifying the first reservation to another offer should work");
        mockMvc.perform(post("/admin/bookings/reservations/booking/1")
                .session(session)
                .param("startDate", getNextMonday().toString())
                .param("startTimeHour", "11")
                .param("startTimeMinute", "30")
                .param("endDate", getNextMonday().toString())
                .param("endTimeHour", "12")
                .param("endTimeMinute", "30")
                .param("holidayKey", "NO_HOLIDAY")
                .param("paymentConfirmed", "true")
                .param("publicBooking", "true")
                .param("offers", ""+offer2.getId())
                .param("comment", "Some other comment")
                .param("calendarWeekDays", CalendarWeekDay.Monday.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/bookings/reservations"));
        
        LOG.info("modifying the first reservation to another date should fail as there is no booking setting");
        mockMvc.perform(post("/admin/bookings/reservations/booking/1")
                .session(session)
                .param("startDate", getNextMonday().plusDays(1).toString())
                .param("startTimeHour", "11")
                .param("startTimeMinute", "30")
                .param("endDate", getNextMonday().toString())
                .param("endTimeHour", "12")
                .param("endTimeMinute", "30")
                .param("holidayKey", "NO_HOLIDAY")
                .param("paymentConfirmed", "true")
                .param("publicBooking", "true")
                .param("offers", ""+offer2.getId())
                .param("comment", "Some other comment")
                .param("calendarWeekDays", CalendarWeekDay.Monday.toString()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().hasErrors());
        
        List<Booking> bookings = bookingDAO.findAll();
        Assert.notNull(bookings);
        Assert.isTrue(bookings.size() == 2, "2 bookings should exist");
        Booking firstBooking = bookings.get(0);
        Assert.isTrue(firstBooking.getBookingDate().equals(getNextMonday()), "start date should be monday");
        Assert.isTrue(firstBooking.getBookingTime().equals(new LocalTime(11, 30)), "start time should be 11:30");
        Assert.isTrue(firstBooking.getDuration().equals(60L), "duration should be 60 min");
        Assert.isTrue(firstBooking.getComment().equals("Some other comment"), "comment should be updated");
        Assert.isTrue(firstBooking.getOffer().equals(offer2), "Offer 2 should be selected");
        
        Booking secondBooking = bookings.get(1);
        Assert.isTrue(secondBooking.getBookingTime().equals(new LocalTime(10, 00)), "start time should be 10:00");
    }
}
