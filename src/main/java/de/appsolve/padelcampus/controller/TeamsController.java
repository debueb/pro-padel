/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.GameDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import de.appsolve.padelcampus.utils.Msg;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/teams")
public class TeamsController extends BaseController{
    
    @Autowired
    TeamDAOI teamDAO;
    
    @Autowired
    PlayerDAOI playerDAO;
    
    @Autowired
    EventDAOI eventDAO;
    
    @Autowired
    GameDAOI gameDAO;
    
    @Autowired
    Msg msg;
    
    @RequestMapping()
    public ModelAndView getIndex(){
        return getIndexView();
    }
    
    @RequestMapping("/all")
    public ModelAndView getAll(){
        return getTeamsView("Alle Teams", teamDAO.findAll());
    }
    
     @RequestMapping("/team/{teamId}")
    public ModelAndView getTeam(@PathVariable("teamId") Long teamId){
        return getTeamView(teamDAO.findById(teamId));
    }
    
    @RequestMapping("/event/{id}")
    public ModelAndView getAllTeamsForEvent(@PathVariable("id") Long eventId){
        Event event = eventDAO.findById(eventId);
        Set<Team> participants = event.getTeams();
        return getTeamsView(msg.get("TeamsIn", new Object[]{event.getName()}), participants);
    }
       
    @RequestMapping("/player/{playerId}")
    public ModelAndView getTeamsForPlayer(@PathVariable("playerId") Long playerId){
        Player player = playerDAO.findById(playerId);
        List<Team> participants = teamDAO.findByPlayer(player);
        return getTeamsView(msg.get("TeamsWith", new Object[]{player.toString()}), participants);
    }

    private ModelAndView getTeamsView(String title, Collection<? extends Participant> teams) {
        ModelAndView mav = new ModelAndView("teams/teams", "Teams", teams);
        mav.addObject("Title", title);
        return mav;
    }

    private ModelAndView getTeamView(Team team) {
        ModelAndView mav = new ModelAndView("teams/team", "Team", team);
        mav.addObject("Games", gameDAO.findByParticipant(team));
        return mav;
    }

    private ModelAndView getIndexView() {
        ModelAndView mav = new ModelAndView("teams/index");
        mav.addObject("Events", eventDAO.findAll());
        return mav;
    }
}
