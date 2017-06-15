/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test.tournaments;

import de.appsolve.padelcampus.constants.EventType;
import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import de.appsolve.padelcampus.test.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author dominik
 */
public class TestGroupKnockoutTournament extends TestBase {
    
    private static final Integer NUM_PLAYERS    = 10;

    @Test
    public void test01CreateNewAccount() throws Exception {
        
        createAdminAccount();
        
        login(ADMIN_EMAIL, ADMIN_PASSWORD);
        
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
        mockMvc.perform(get("/admin/teams/add")
                .session(session))
                .andExpect(status().isOk());
        
        for (int i=0; i<NUM_PLAYERS; i=i+2){
            Player first = playerDAO.findByEmail("testplayer"+i+"@appsolve.de");
            Player second = playerDAO.findByEmail("testplayer"+(i+1)+"@appsolve.de");
            mockMvc.perform(post("/admin/teams/add")
                .session(session)
                .param("players", first.getUUID(), second.getUUID()))
                .andExpect(status().is3xxRedirection());
                     
        }
        
        List<String> teamUUIDs = new ArrayList<>();
        for (long i=0; i<NUM_PLAYERS; i=i+2){
            String teamName = String.format("Player %s Test %s / Player %s Test %s", i, i, i+1, i+1);
            Team team = teamDAO.findByAttribute("name", teamName);
            Assert.assertNotNull("team must be findable by name", team);
            teamUUIDs.add(team.getUUID());
        }
        
        LOG.info("creating new tournament");
        mockMvc.perform(post("/admin/events/add")
            .session(session)
            .param("name", "Knockout Tournament")
            .param("gender", Gender.male.name())
            .param("eventType", EventType.Knockout.name())
            .param("startDate", "2016-05-06")
            .param("endDate", "2016-05-06")
            .param("active", "on")
            .param("maxNumberOfParticipants", "4")
            .param("participants", teamUUIDs.toArray(new String[teamUUIDs.size()])))
            .andExpect(status().is3xxRedirection());
        
        Event event = eventDAO.findByAttribute("name", "Knockout Tournament");
        
        LOG.info("attempt to modify tournament should fail");
        mockMvc.perform(post("/admin/events/edit/"+event.getId())
            .session(session)
            .param("id", event.getId().toString())
            .param("name", "GroupKnockout Tournament")
            .param("gender", Gender.male.name())
            .param("eventType", EventType.GroupKnockout.name())
            .param("startDate", "2016-05-06")
            .param("endDate", "2016-05-06")
            .param("active", "on")
            .param("participants", teamUUIDs.toArray(new String[teamUUIDs.size()])))
            .andExpect(model().hasErrors());

        
        LOG.info("update group draws");
        mockMvc.perform(post("/admin/events/edit/"+event.getId()+"/groupdraws")
            .session(session)
            .param("_groupParticipants[0]", "1")
            .param("groupParticipants[0]", teamUUIDs.get(0))
            .param("groupParticipants[0]", teamUUIDs.get(1))
            .param("groupParticipants[0]", teamUUIDs.get(2))
            .param("_groupParticipants[1]", "1")
            .param("groupParticipants[1]", teamUUIDs.get(3))
            .param("groupParticipants[1]", teamUUIDs.get(4)))
            .andExpect(status().is3xxRedirection());
        
        LOG.info("check tournament view");
        
        mockMvc.perform(get("/events/event/"+event.getId())
            .session(session))
            .andExpect(status().isOk())
            .andExpect(model().hasNoErrors())
            .andExpect(model().attributeExists("Model"));
        
        mockMvc.perform(get("/events/event/"+event.getId()+"/groupgames")
            .session(session))
            .andExpect(status().isOk())
            .andExpect(model().hasNoErrors())
            .andExpect(view().name("events/groupknockout/groupgames"))
            .andExpect(model().attributeExists("GroupParticipantGameResultMap"));
                
        mockMvc.perform(get("/events/event/"+event.getId()+"/knockoutgames")
            .session(session))
            .andExpect(status().isOk())
            .andExpect(model().hasNoErrors())
            .andExpect(view().name("events/groupknockout/knockoutgamesend"));
            //.andExpect(new ModelAttributeToStringEquals("RoundGameMap", "{0=[Team 0, Team 6 vs. Team 8, Team 2, Team 4], 1=[Team 0, Team 2 vs. Team 4], 2=[]}"));
    }
}
