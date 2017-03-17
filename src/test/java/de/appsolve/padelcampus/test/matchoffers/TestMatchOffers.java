/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test.matchoffers;

import de.appsolve.padelcampus.db.dao.MatchOfferDAOI;
import de.appsolve.padelcampus.db.model.MatchOffer;
import de.appsolve.padelcampus.test.*;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author dominik
 */
public class TestMatchOffers extends TestBase {
    
    @Autowired
    MatchOfferDAOI matchOfferDAO;
    
    @Test
    public void testMatchOfferCreationAndDeletion() throws Exception {
        
        createAdminAccount();
        
        LOG.info("Creating new account "+USER_EMAIL);
        mockMvc.perform(post("/login/register")
                .session(session)
                .param("firstName", "user")
                .param("lastName", "test")
                .param("email", USER_EMAIL)
                .param("phone", "01739398758")
                .param("password", USER_PASSWORD))
                .andExpect(status().isOk());
        logout();
        
        LOG.info("Attempt to create matchoffer without being logged in should result in login view");
        mockMvc.perform(post("/matchoffers/add")
                .session(session)
                .param("startDate", getNextMonday().toString())
                .param("startTimeHour", "10")
                .param("startTimeMinute", "00")
                .param("duration", "60")
                .param("minPlayersCount", "4")
                .param("maxPlayersCount", "4")
                .param("skillLevels", "Level1")
                .param("_skillLevels", "1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("include/loginrequired"));
        
        login(ADMIN_EMAIL, ADMIN_PASSWORD);
        
        LOG.info("Creating a matchoffer as logged in user should work");
        mockMvc.perform(post("/matchoffers/add")
                .session(session)
                .param("startDate", getNextMonday().toString())
                .param("startTimeHour", "10")
                .param("startTimeMinute", "00")
                .param("duration", "60")
                .param("minPlayersCount", "4")
                .param("maxPlayersCount", "4")
                .param("skillLevels", "Level1")
                .param("_skillLevels", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/matchoffers/1"));
        
        
        LOG.info("Attempting to delete a matchoffer without being logged in should result in login view");
        logout();
        mockMvc.perform(get("/matchoffers/1/delete")
                .session(session))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("include/loginrequired"));
        
        mockMvc.perform(post("/matchoffers/1/delete")
                .session(session))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("include/loginrequired"));
        
        
        LOG.info("Attempting to delete a matchoffer as a different user should fail");
        login(USER_EMAIL, USER_PASSWORD);
        mockMvc.perform(get("/matchoffers/1/delete")
                .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/matchoffers"));
        
        mockMvc.perform(post("/matchoffers/1/delete")
                .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/matchoffers"));
        
        MatchOffer matchOffer = matchOfferDAO.findById(1L);
        Assert.assertNotNull("MatchOffer must exist", matchOffer);
        
        
        LOG.info("Attempting to delete a matchoffer as admin should work");
        logout();
        login(ADMIN_EMAIL, ADMIN_PASSWORD);
        mockMvc.perform(get("/matchoffers/1/delete")
                .session(session))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/include/delete"));
        
        mockMvc.perform(post("/matchoffers/1/delete")
                .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/matchoffers"));
        
        matchOffer = matchOfferDAO.findById(1L);
        Assert.assertNull("MatchOffer must no longer exist", matchOffer);
    }
}
