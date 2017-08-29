/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test.events;

import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.constants.EventType;
import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Player;
import org.joda.time.LocalDate;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.util.Assert;

/**
 *
 * @author dominik
 */
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventBookingPullRoundRobinTest extends EventBookingTestBase {
    
    @Test
    public void test01AddPullEvent() throws Exception {
        createAdminAccount();
        login(ADMIN_EMAIL, ADMIN_PASSWORD);
        
        Player player1 = createPlayer(1);
        Player player2 = createPlayer(2);
        Player player3 = createPlayer(3);
        
        LocalDate nextMonday = getNextMonday();
        
        LOG.info("GET /admin");
        mockMvc.perform(get("/admin/")
                .session(session))
                .andExpect(status().is2xxSuccessful());
        
        LOG.info("GET /admin/events");
        mockMvc.perform(get("/admin/events")
                .session(session))
                .andExpect(status().is2xxSuccessful());
        
        LOG.info("GET /admin/events/add");
        mockMvc.perform(get("/admin/events/add")
                .session(session))
                .andExpect(status().is2xxSuccessful());
        
        LOG.info("POST /admin/events/add - add new Pull event");
        mockMvc.perform(post("/admin/events/add")
                .session(session)
                .param("name", "Test Booking Event")
                .param("description", "Test Event description")
                .param("gender", Gender.male.toString())
                .param("eventType", EventType.PullRoundRobin.toString())
                .param("startDate", nextMonday.toString())
                .param("startTimeHour", "10")
                .param("startTimeMinute", "00")
                .param("endDate", nextMonday.toString())
                .param("location", "Test location")
                .param("numberOfSets", "3")
                .param("numberOfGamesPerSet", "7")
                .param("numberOfGamesInFinalSet", "1")
                .param("maxNumberOfParticipants", "2")
                .param("paymentMethods", PaymentMethod.Cash.toString(), PaymentMethod.PayPal.toString())
                .param("price", "20.00")
                .param("active", "true")
                .param("allowSignup", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/events"));
    
        Event event = eventDAO.findByAttribute("name", "Test Booking Event");
        Assert.notNull(event, "Event has not been created");
        
        LOG.info("GET /events/event/"+event.getId());
        mockMvc.perform(get("/events/event/"+event.getId())
                .session(session))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("events/event"));
        
        LOG.info("GET /events/bookings/"+event.getId()+"/participate - make sure user is logged in");
        mockMvc.perform(get("/events/bookings/"+event.getId()+"/participate"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("include/loginrequired"))
                .andExpect(model().attribute("redirectURL", "/events/bookings/"+event.getId()+"/participate"));
        
        login(player1, event);
        participate(event);
        logout();
        
        login(player2, event);
        participate(event);
        logout();
        
        login(player3, event);
        LOG.info("POST /events/bookings/"+event.getId()+"/participate - prevent overbooking");        
        mockMvc.perform(post("/events/bookings/"+event.getId()+"/participate")
                .session(session)
                .param("paymentMethod", PaymentMethod.Cash.toString()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().hasErrors());
    }
    
    private void participate(Event event) throws Exception {
        LOG.info("GET /events/bookings/"+event.getId()+"/participate");        
        mockMvc.perform(get("/events/bookings/"+event.getId()+"/participate")
                .session(session))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("events/bookings/participate/pull"));
        
        LOG.info("POST /events/bookings/"+event.getId()+"/participate");        
        mockMvc.perform(post("/events/bookings/"+event.getId()+"/participate")
                .session(session)
                .param("paymentMethod", PaymentMethod.Cash.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events/bookings/"+event.getId()+"/confirm"));
        
        LOG.info("GET /events/bookings/"+event.getId()+"/confirm");
        mockMvc.perform(get("/events/bookings/"+event.getId()+"/confirm")
                .session(session))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeDoesNotExist("error"));
        
        LOG.info("POST /events/bookings/"+event.getId()+"/confirm - make sure user accepts cancellation policy");
        mockMvc.perform(post("/events/bookings/"+event.getId()+"/confirm")
                .session(session))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("error"));
        
        Booking booking = (Booking) session.getAttribute(Constants.SESSION_BOOKING);
        
        LOG.info("POST /events/bookings/"+event.getId()+"/confirm");
        mockMvc.perform(post("/events/bookings/"+event.getId()+"/confirm")
                .session(session)
                .param("accept-cancellation-policy", "on"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events/bookings/"+booking.getUUID()+"/success"));     
    
        LOG.info("GET /events/bookings/"+booking.getUUID()+"/success");
        mockMvc.perform(get("/events/bookings/"+booking.getUUID()+"/success")
                .session(session))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeDoesNotExist("error"));
        
        LOG.info("POST /events/bookings/"+event.getId()+"/participate - prevent duplicate participation");
        mockMvc.perform(post("/events/bookings/"+event.getId()+"/participate")
                .session(session)
                .param("paymentMethod", PaymentMethod.Cash.toString()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().hasErrors());
        
        LOG.info("GET /events/bookings/"+event.getId()+"/confirm - prevent duplicate participation");
        mockMvc.perform(get("/events/bookings/"+event.getId()+"/confirm")
                .session(session))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("error"));
        
        LOG.info("POST /events/bookings/"+event.getId()+"/confirm - prevent duplicate participation");
        mockMvc.perform(post("/events/bookings/"+event.getId()+"/confirm")
                .session(session)
                .param("accept-cancellation-policy", "on"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("error"));
    }
}
