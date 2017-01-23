/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test.events;

import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.test.TestBase;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author dominik
 */
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class TestEventBookingBase extends TestBase {
    
    protected void login(Player player, Event event) throws Exception {
        LOG.info("GET /login?redirect=/events/bookings/"+event.getId()+"/participate");
        mockMvc.perform(get("/login?redirect=/events/bookings/"+event.getId()+"/participate")
                .session(session))
                .andExpect(status().is2xxSuccessful());
        
        LOG.info("POST /login?redirect=/events/bookings/"+event.getId()+"/participate");
        mockMvc.perform(post("/login?redirect=/events/bookings/"+event.getId()+"/participate")
                .session(session)
                .param("email", player.getEmail())
                .param("password", player.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events/bookings/"+event.getId()+"/participate"));
    }

    protected Player createPlayer(int i) {
        Player player = new Player();
        player.setEmail("padelcampus-unittest-eventbooking"+i+"@appsolve.de");
        player.setFirstName("dummy "+RandomStringUtils.random(3, "abcdefghijklmnopqrstuvwxyz".toCharArray()));
        player.setLastName("dummy lastname");
        player.setPassword("test");
        player.setPhone("004917497568349");
        return playerDAO.saveOrUpdate(player);
    }
}
