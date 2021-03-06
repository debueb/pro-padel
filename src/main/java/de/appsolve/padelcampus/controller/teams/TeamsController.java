/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.teams;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.GameDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Team;
import de.appsolve.padelcampus.exceptions.ResourceNotFoundException;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/teams")
@Transactional
public class TeamsController extends BaseController {

    @Autowired
    TeamDAOI teamDAO;

    @Autowired
    PlayerDAOI playerDAO;

    @Autowired
    EventDAOI eventDAO;

    @Autowired
    GameDAOI gameDAO;

    @RequestMapping()
    public ModelAndView getIndex() {
        return getIndexView();
    }

    @RequestMapping("/all")
    public ModelAndView getAll() {
        List<Event> events = eventDAO.findAllActiveFetchWithParticipantsAndPlayers();
        List<Team> teams = new ArrayList<>();
        for (Event event : events) {
            if (event.getActive()) {
                teams.addAll(event.getTeams());
            }
        }
        Collections.sort(teams);
        return getTeamsView(null, "Alle Teams", teams);
    }

    @RequestMapping("/team/{teamUUID}")
    public ModelAndView getTeam(@PathVariable("teamUUID") String teamUUID) {
        return getTeamView(teamDAO.findByUUIDFetchWithPlayers(teamUUID));
    }

    @RequestMapping("/event/{id}")
    public ModelAndView getAllTeamsForEvent(@PathVariable("id") Long eventId) {
        Event event = eventDAO.findByIdFetchWithParticipants(eventId);
        Set<Team> participants = event.getTeams();
        return getTeamsView(event, msg.get("TeamsIn", new Object[]{event.getName()}), participants);
    }

    private ModelAndView getTeamsView(Event event, String title, Collection<? extends Participant> teams) {
        ModelAndView mav = new ModelAndView("teams/teams", "Teams", teams);
        mav.addObject("Model", event);
        mav.addObject("Title", title);
        return mav;
    }

    private ModelAndView getTeamView(Team team) {
        if (team == null) {
            throw new ResourceNotFoundException();
        }
        ModelAndView mav = new ModelAndView("teams/team", "Team", team);

        //get upcoming events
        List<Event> currentEvents = eventDAO.findAllUpcomingWithParticipant(team);

        //get past events from actual game data so that teams that have not played will not be shown and so that pull events are considered
        Set<Event> pastEvents = new TreeSet<>();
        LocalDate today = LocalDate.now();
        List<Game> games = gameDAO.findByParticipant(team);
        for (Game game : games) {
            Event event = game.getEvent();
            if (today.isAfter(event.getEndDate())) {
                pastEvents.add(event);
            }
        }
        mav.addObject("Events", currentEvents);
        mav.addObject("PastEvents", pastEvents);
        return mav;
    }

    private ModelAndView getIndexView() {
        ModelAndView mav = new ModelAndView("teams/index");
        mav.addObject("Events", eventDAO.findAllActive());
        return mav;
    }
}
