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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static de.appsolve.padelcampus.test.matchers.GlobalErrorsMatcher.globalErrors;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author dominik
 */
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventBookingCommunityRoundRobinTest extends EventBookingTestBase {

    private static final String TEST_EVENT_NAME = "CommunityRoundRobin Event";
    private static final Integer NUMBER_OF_PLAYERS = 7;

    @Test
    public void test01CommunityEvent() throws Exception {
        createAdminAccount();
        login(ADMIN_EMAIL, ADMIN_PASSWORD);

        List<Player> players = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            players.add(createPlayer(i));
        }

        LocalDate nextMonday = getNextMonday();

        LOG.info("POST /admin/events/add - add new CommunityRoundRobin event");
        mockMvc.perform(post("/admin/events/add")
                .session(session)
                .param("name", TEST_EVENT_NAME)
                .param("description", "Test Event description")
                .param("gender", Gender.male.toString())
                .param("eventType", EventType.CommunityRoundRobin.toString())
                .param("startDate", nextMonday.toString())
                .param("startTimeHour", "10")
                .param("startTimeMinute", "00")
                .param("endDate", nextMonday.toString())
                .param("location", "Test location")
                .param("numberOfSets", "3")
                .param("numberOfGamesPerSet", "7")
                .param("numberOfGamesInFinalSet", "1")
                .param("maxNumberOfParticipants", "" + (NUMBER_OF_PLAYERS - 1))
                .param("paymentMethods", PaymentMethod.Cash.toString(), PaymentMethod.PayPal.toString())
                .param("price", "20.00")
                .param("active", "true")
                .param("allowSignup", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/events"));

        Event event = eventDAO.findByAttribute("name", TEST_EVENT_NAME);
        Assert.notNull(event, "Event has been created");

        login(players.get(0), event);
        participate(players.subList(1, 3), event);
        logout();

        login(players.get(3), event);
        participate(players.subList(4, 6), event);
        logout();

        login(players.get(6), event);
        LOG.info("POST /events/bookings/" + event.getId() + "/participate - prevent registering with partner who is already registered");
        mockMvc.perform(post("/events/bookings/" + event.getId() + "/participate")
                .session(session)
                .param("paymentMethod", PaymentMethod.Cash.toString())
                .param("community.name", "Team XYZ")
                .param("players", players.get(0).getUUID())
                .param("_players", "1")
                .param("_newPlayers", "0"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().hasErrors())
                .andExpect(globalErrors().hasGlobalError("EventBookingRequest", "*", msg.get("AlreadyParticipatesInThisEvent", new Object[]{players.get(0)})));
    }

    private void participate(List<Player> players, Event event) throws Exception {
        LOG.info("GET /events/bookings/" + event.getId() + "/participate");
        mockMvc.perform(get("/events/bookings/" + event.getId() + "/participate")
                .session(session))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("events/bookings/participate/community"))
                .andExpect(model().hasNoErrors());

        LOG.info("POST /events/bookings/" + event.getId() + "/participate");
        MockHttpServletRequestBuilder requestBuilder = post("/events/bookings/" + event.getId() + "/participate")
                .session(session)
                .param("paymentMethod", PaymentMethod.Cash.toString())
                .param("community.name", "Team XYZ")
                .param("_players", "" + players.size())
                .param("_newPlayers", "0");
        players.forEach(player -> requestBuilder.param("players", player.getUUID()));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events/bookings/" + event.getId() + "/confirm"));

        LOG.info("GET /events/bookings/" + event.getId() + "/confirm");
        mockMvc.perform(get("/events/bookings/" + event.getId() + "/confirm")
                .session(session))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeDoesNotExist("error"));

        LOG.info("POST /events/bookings/" + event.getId() + "/confirm - make sure user accepts cancellation policy");
        mockMvc.perform(post("/events/bookings/" + event.getId() + "/confirm")
                .session(session))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("error"));

        Booking booking = (Booking) session.getAttribute(Constants.SESSION_BOOKING);

        LOG.info("POST /events/bookings/" + event.getId() + "/confirm");
        mockMvc.perform(post("/events/bookings/" + event.getId() + "/confirm")
                .session(session)
                .param("accept-cancellation-policy", "on"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events/bookings/" + booking.getUUID() + "/success"));

        LOG.info("GET /events/bookings/" + booking.getUUID() + "/success");
        mockMvc.perform(get("/events/bookings/" + booking.getUUID() + "/success")
                .session(session))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeDoesNotExist("error"));

        LOG.info("POST /events/bookings/" + event.getId() + "/participate - prevent duplicate participation");
        mockMvc.perform(post("/events/bookings/" + event.getId() + "/participate")
                .session(session)
                .param("paymentMethod", PaymentMethod.Cash.toString())
                .param("community.name", "Team XYZ")
                .param("_players", "0")
                .param("_newPlayers", "0"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().hasErrors());

        LOG.info("GET /events/bookings/" + event.getId() + "/confirm - prevent duplicate participation");
        mockMvc.perform(get("/events/bookings/" + event.getId() + "/confirm")
                .session(session))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("error"));

        LOG.info("POST /events/bookings/" + event.getId() + "/confirm - prevent duplicate participation");
        mockMvc.perform(post("/events/bookings/" + event.getId() + "/confirm")
                .session(session)
                .param("accept-cancellation-policy", "on"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("error"));
    }
}
