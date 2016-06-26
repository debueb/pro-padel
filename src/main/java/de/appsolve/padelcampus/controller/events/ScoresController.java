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
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.utils.RankingUtil;
import java.util.List;
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
@RequestMapping("/scores")
public class ScoresController extends BaseController{
    
    @Autowired
    EventDAOI eventDAO;
    
    @Autowired
    GameDAOI gameDAO;
    
    @Autowired
    GameSetDAOI gameSetDAO;
    
    @Autowired
    RankingUtil rankingUtil;
    
    @RequestMapping
    public ModelAndView getIndex(){
        ModelAndView mav = new ModelAndView("scores/index");
        mav.addObject("Events", eventDAO.findAllActive());
        return mav;
    }
    
    @RequestMapping("/event/{eventId}")
    public ModelAndView getEvent(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithParticipants(eventId);
        List<Game> eventGames = gameDAO.findByEvent(event);
        List<ScoreEntry> scoreEntries = rankingUtil.getScores(event.getParticipants(), eventGames);
        
        ModelAndView mav = new ModelAndView("scores/event", "Event", event);
        mav.addObject("ScoreEntries", scoreEntries);
        return mav;
    }
}
