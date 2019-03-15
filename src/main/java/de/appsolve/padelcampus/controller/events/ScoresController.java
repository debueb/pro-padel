/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.events;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.ScoreEntry;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.GameDAOI;
import de.appsolve.padelcampus.db.dao.GameSetDAOI;
import de.appsolve.padelcampus.db.model.*;
import de.appsolve.padelcampus.exceptions.ResourceNotFoundException;
import de.appsolve.padelcampus.utils.RankingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/scores")
public class ScoresController extends BaseController {

    @Autowired
    EventDAOI eventDAO;

    @Autowired
    GameDAOI gameDAO;

    @Autowired
    GameSetDAOI gameSetDAO;

    @Autowired
    RankingUtil rankingUtil;

    @RequestMapping
    public ModelAndView getIndex() {
        ModelAndView mav = new ModelAndView("scores/index");
        mav.addObject("Events", eventDAO.findAllActive());
        return mav;
    }

    @RequestMapping("/event/{eventId}")
    public ModelAndView getEvent(@PathVariable("eventId") Long eventId) {
        Event event = eventDAO.findByIdFetchWithParticipants(eventId);
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        List<Game> eventGames = gameDAO.findByEventWithPlayers(event);
        List<ScoreEntry> scoreEntries;
        switch (event.getEventType()) {
            case PullRoundRobin:
                scoreEntries = rankingUtil.getPullResults(eventGames);
                break;
            case FriendlyGames:
                //friendly games do not have participants. get them from all games
                Set<Participant> participants = new HashSet<>();
                for (Game game : eventGames) {
                    participants.addAll(game.getParticipants());
                }
                scoreEntries = rankingUtil.getScores(participants, eventGames);
                break;
            default:
                scoreEntries = rankingUtil.getScores(event.getParticipants(), eventGames);
        }

        ModelAndView mav = new ModelAndView("scores/event", "Model", event);
        mav.addObject("ScoreEntries", scoreEntries);
        return mav;
    }
}
