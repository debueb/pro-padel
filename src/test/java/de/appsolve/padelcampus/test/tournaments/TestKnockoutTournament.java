/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test.tournaments;

import de.appsolve.padelcampus.constants.EventType;
import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.constants.Privilege;
import de.appsolve.padelcampus.db.model.AdminGroup;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.test.*;
import de.appsolve.padelcampus.test.matchers.ModelAttributeToStringEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.junit.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author dominik
 */
public class TestKnockoutTournament extends TestBase {
    
    
    private static final String ADMIN_EMAIL     = "admin@appsolve.de";
    private static final String ADMIN_PASSWORD  = "test";
    private static final Integer NUM_PLAYERS    = 10;

    private static final Logger LOG = Logger.getLogger(TestKnockoutTournament.class);
    
    @Test
    public void test01CreateNewAccount() throws Exception {
        
        LOG.info("Creating new account");
        mockMvc.perform(post("/login/register")
                .session(session)
                .param("firstName", "admin")
                .param("lastName", "test")
                .param("email", ADMIN_EMAIL)
                .param("phone", "01739398758")
                .param("password", ADMIN_PASSWORD))
                .andExpect(status().isOk());
        
     
        LOG.info("Promoting account to admin");
        AdminGroup adminGroup = new AdminGroup();
        adminGroup.setName("admins");
        adminGroup.setPrivileges(new HashSet<>(Arrays.asList(new Privilege[]{Privilege.AccessAdminInterface, Privilege.ManagePlayers, Privilege.ManageTeams})));
        Player admin = playerDAO.findByEmail(ADMIN_EMAIL);
        adminGroup.setPlayers(new HashSet<>(Arrays.asList(new Player[]{admin})));
        adminGroupDAO.saveOrUpdate(adminGroup);
        
        LOG.info("login to admin account");
        mockMvc.perform(post("/login")
                .session(session)
                .param("email", ADMIN_EMAIL)
                .param("password", ADMIN_PASSWORD))
                .andExpect(status().is3xxRedirection());
    
        LOG.info("creating new players");
        for (int i=0; i<NUM_PLAYERS; i++){
             mockMvc.perform(post("/admin/players/add")
                .session(session)
                .param("firstName", "Player "+i)
                .param("lastName", "Test "+i)
                .param("email", "testplayer"+i+"@appsolve.de")
                .param("phone", "01739398758")
                .param("gender", "male"))
                .andExpect(status().is3xxRedirection());
        }
     
        LOG.info("creating new teams");
        mockMvc.perform(get("/admin/teams/add"))
                .andExpect(status().isOk());
        
        for (int i=0; i<NUM_PLAYERS; i=i+2){
            Player first = playerDAO.findByEmail("testplayer"+i+"@appsolve.de");
            Player second = playerDAO.findByEmail("testplayer"+(i+1)+"@appsolve.de");
            mockMvc.perform(post("/admin/teams/add")
                .session(session)
                .param("name", "Team "+i)
                .param("players", first.getUUID(), second.getUUID()))
                .andExpect(status().is3xxRedirection());
                     
        }
        
        
        List<String> teamUUIDs = new ArrayList<>();
        for (long i=0; i<NUM_PLAYERS; i=i+2){
            teamUUIDs.add(teamDAO.findByAttribute("name", "Team "+i).getUUID());
        }
        
        LOG.info("creating new knockout tournament");
        mockMvc.perform(post("/admin/events/add")
            .session(session)
            .param("name", "Knockout Tournament")
            .param("gender", Gender.male.name())
            .param("eventType", EventType.Knockout.name())
            .param("startDate", "2016-05-06")
            .param("endDate", "2016-05-06")
            .param("active", "on")
            .param("participants", teamUUIDs.toArray(new String[teamUUIDs.size()])))
            .andExpect(status().is3xxRedirection());
        
        LOG.info("attempt to modify knockout tournament should fail");
        mockMvc.perform(post("/admin/events/edit/1")
            .session(session)
            .param("id", "1")
            .param("name", "Knockout Tournament")
            .param("gender", Gender.male.name())
            .param("eventType", EventType.GroupKnockout.name())
            .param("startDate", "2016-05-06")
            .param("endDate", "2016-05-06")
            .param("active", "on")
            .param("participants", teamUUIDs.toArray(new String[teamUUIDs.size()])))
            .andExpect(model().hasErrors());

        
        //update games
        LOG.info("check tournament view");
        
        mockMvc.perform(get("/events/event/1")
            .session(session))
            .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(new ModelAttributeToStringEquals("RoundGameMap", "{0=[Team 0, Team 6 vs. Team 8, Team 2, Team 4], 1=[Team 0, Team 2 vs. Team 4], 2=[]}"))
                ;
               
    }
}
