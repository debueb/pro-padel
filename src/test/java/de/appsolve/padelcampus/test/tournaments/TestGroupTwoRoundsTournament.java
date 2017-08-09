/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test.tournaments;

import static de.appsolve.padelcampus.constants.Constants.FIRST_SET;
import de.appsolve.padelcampus.constants.EventType;
import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import de.appsolve.padelcampus.test.*;
import static de.appsolve.padelcampus.test.matchers.GlobalErrorsMatcher.globalErrors;
import de.appsolve.padelcampus.utils.FormatUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author dominik
 */
public class TestGroupTwoRoundsTournament extends TestBase {
    
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
                .param("email", "testplayer"+i+"@pro-padel.de")
                .param("phone", "01739398758")
                .param("gender", "male"))
                .andExpect(status().is3xxRedirection());
        }
     
        LOG.info("creating new teams");
        mockMvc.perform(get("/admin/teams/add")
                .session(session))
                .andExpect(status().isOk());
        
        for (int i=0; i<NUM_PLAYERS; i=i+2){
            Player first = playerDAO.findByEmail("testplayer"+i+"@pro-padel.de");
            Player second = playerDAO.findByEmail("testplayer"+(i+1)+"@pro-padel.de");
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
            .param("name", "GroupTwoRounds Tournament")
            .param("gender", Gender.male.name())
            .param("eventType", EventType.GroupTwoRounds.name())
            .param("startDate", "2016-05-06")
            .param("endDate", "2016-05-06")
            .param("active", "on")
            .param("maxNumberOfParticipants", "4")
            .param("participants", teamUUIDs.toArray(new String[teamUUIDs.size()])))
            .andExpect(status().is3xxRedirection());
        
        Event event = eventDAO.findByAttribute("name", "GroupTwoRounds Tournament");
        
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
        mockMvc.perform(post("/admin/events/edit/"+event.getId()+"/groupdraws/0")
            .session(session)
            .param("_groupParticipants[0]", "1")
            .param("groupParticipants[0]", teamUUIDs.get(0))
            .param("groupParticipants[0]", teamUUIDs.get(1))
            .param("groupParticipants[0]", teamUUIDs.get(2))
            .param("_groupParticipants[1]", "1")
            .param("groupParticipants[1]", teamUUIDs.get(3))
            .param("groupParticipants[1]", teamUUIDs.get(4)))
            .andExpect(status().is3xxRedirection());
        
        event = eventDAO.findByIdFetchWithGames(event.getId());
        assertNotNull("saving draws should create games", event.getGames());
        assertTrue("saving draws should create games", !event.getGames().isEmpty());
        
        LOG.info("check tournament view");
        
        mockMvc.perform(get("/events/event/"+event.getId())
            .session(session))
            .andExpect(status().isOk())
            .andExpect(model().hasNoErrors())
            .andExpect(model().attributeExists("Model"));
        
        mockMvc.perform(get("/events/event/"+event.getId()+"/groupgames/0")
            .session(session))
            .andExpect(status().isOk())
            .andExpect(model().hasNoErrors())
            .andExpect(view().name("events/groupknockout/groupgames"))
            .andExpect(model().attributeExists("GroupParticipantGameResultMap"));
                
        mockMvc.perform(get("/admin/events/event/"+event.getId()+"/groupgamesend/0")
            .session(session))
            .andExpect(status().isOk())
            .andExpect(model().hasNoErrors())
            .andExpect(model().attributeExists("Round"))
            .andExpect(view().name("admin/events/groupgamesend"));
        
        mockMvc.perform(post("/admin/events/event/"+event.getId()+"/groupgamesend/0")
            .session(session))
            .andExpect(status().isOk())
            .andExpect(model().hasErrors())
            .andExpect(globalErrors().hasGlobalError("Model", "*", msg.get("CannotEndGroupGames")))
            .andExpect(view().name("admin/events/groupgamesend"));
        
        for (Game game: event.getGames()){
            mockMvc.perform(get("/games/game/"+game.getId()+"/edit")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("Game"))
                .andExpect(model().attributeExists("GamesMap"))
                .andExpect(view().name("games/edit"));
            
            String redirectUrl = "events/event/"+event.getId()+"/groupgames/0";
            MockHttpServletRequestBuilder builder = post("/games/game/"+game.getId()+"/edit")
                    .param("startDate", getLastMonday().toString(FormatUtils.DATE_HUMAN_READABLE))
                    .param("redirectUrl", redirectUrl)
                    .session(session);
            for (Participant participant: game.getParticipants()){
                for (int set=FIRST_SET; set<=event.getNumberOfSets(); set++){
                    builder = builder.param("game-"+game.getId()+"-participant-"+participant.getId()+"-set-"+set, "1");
                }
            }
            mockMvc.perform(builder)
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"+redirectUrl));
        }
        
        mockMvc.perform(post("/admin/events/event/"+event.getId()+"/groupgamesend/0")
            .session(session))
            .andExpect(status().is3xxRedirection())
            .andExpect(model().hasNoErrors())
            .andExpect(redirectedUrl("/admin/events/edit/"+event.getId()+"/groupdraws/1"));
        
        mockMvc.perform(get("/admin/events/edit/"+event.getId()+"/groupdraws/1")
            .session(session))
            .andExpect(status().is2xxSuccessful())
            .andExpect(model().hasNoErrors())
            .andExpect(view().name("admin/events/groupdraws"));
        
        mockMvc.perform(post("/admin/events/edit/"+event.getId()+"/groupdraws/1")
            .session(session)
            .param("_groupParticipants[0]", "1")
            .param("groupParticipants[0]", teamUUIDs.get(0))
            .param("groupParticipants[0]", teamUUIDs.get(1))
            .param("groupParticipants[0]", teamUUIDs.get(2))
            .param("_groupParticipants[1]", "1")
            .param("groupParticipants[1]", teamUUIDs.get(3))
            .param("groupParticipants[1]", teamUUIDs.get(4)))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/events/event/"+event.getId()+"/groupgames/1"));
    }
}
