/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.events;

import de.appsolve.padelcampus.controller.BaseController;
import static de.appsolve.padelcampus.constants.Constants.MATCH_PLAY_FACTOR;
import static de.appsolve.padelcampus.constants.Constants.MATCH_WIN_FACTOR;
import de.appsolve.padelcampus.data.ScoreEntry;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.GameDAOI;
import de.appsolve.padelcampus.db.dao.GameSetDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.db.model.GameSet;
import de.appsolve.padelcampus.db.model.Participant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    
    private List<GameSet> eventGameSets = new ArrayList<>();
    
    @RequestMapping
    public ModelAndView getIndex(){
        ModelAndView mav = new ModelAndView("scores/index");
        mav.addObject("Events", eventDAO.findAll());
        return mav;
    }
    
    @RequestMapping("/event/{eventId}")
    public ModelAndView getEvent(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        List<Game> eventGames = gameDAO.findByEvent(event);
        eventGameSets = gameSetDAO.findByEvent(event);
        List<ScoreEntry> scoreEntries = new ArrayList<>();
        for (Participant participant: event.getParticipants()){
            int matchesPlayed   = 0;
            int matchesWon      = 0;
            int totalSetsPlayed = 0;
            int totalSetsWon    = 0;
            int totalGamesPlayed= 0;
            int totalGamesWon   = 0;
                
            for (Game game: eventGames){
                if (game.getParticipants().contains(participant)){
                    int setsPlayed      = 0;
                    int setsWon         = 0;
                
                    Map<Integer, Integer> setMapP1 = getSetMapForParticipant(game, participant);
                    Map<Integer, Integer> setMapP2 = new HashMap<>();
                    for (Participant opponent: game.getParticipants()){
                        if (!opponent.equals(participant)){
                            setMapP2 = getSetMapForParticipant(game, opponent);
                            break;
                        }
                    }

                    for (Integer set: setMapP1.keySet()){
                        setsPlayed++;
                        totalSetsPlayed++;
                        int gamesWonP1  = setMapP1.get(set) == null ? 0 : setMapP1.get(set);
                        int gamesWonP2  = setMapP2.get(set) == null ? 0 : setMapP2.get(set);
                        totalGamesWon    += gamesWonP1;
                        setsWon          += gamesWonP1 > gamesWonP2 ? 1 : 0;
                        totalSetsWon     += gamesWonP1 > gamesWonP2 ? 1 : 0;
                        totalGamesPlayed += gamesWonP1 + gamesWonP2;
                    }

                    if (setsPlayed>0){
                        matchesPlayed++;
                        matchesWon += setsWon > (setsPlayed-setsWon) ? 1 : 0;
                    }
                }
            }
            
            ScoreEntry entry = new ScoreEntry();
            entry.setParticipant(participant);
            entry.setTotalPoints(matchesWon*MATCH_WIN_FACTOR+(matchesPlayed-matchesWon)*MATCH_PLAY_FACTOR);
            entry.setMatchesPlayed(matchesPlayed);
            entry.setMatchesWon(matchesWon);
            entry.setSetsPlayed(totalSetsPlayed);
            entry.setSetsWon(totalSetsWon);
            entry.setGamesWon(totalGamesWon);
            entry.setGamesPlayed(totalGamesPlayed);

            scoreEntries.add(entry);
        }
        Collections.sort(scoreEntries);
        
        ModelAndView mav = new ModelAndView("scores/event", "Event", event);
        mav.addObject("ScoreEntries", scoreEntries);
        return mav;
    }

    private Map<Integer, Integer> getSetMapForParticipant(Game game, Participant participant) {
        Map<Integer, Integer> setMap = new HashMap<>();
        for (GameSet set: eventGameSets){
            if (set.getGame().equals(game) && set.getParticipant().equals(participant)){
                setMap.put(set.getSetNumber(), set.getSetGames());
            }
        }
        return setMap;
    }
}
