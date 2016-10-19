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
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import de.appsolve.padelcampus.utils.RankingUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        List<Game> eventGames = gameDAO.findByEventWithPlayers(event);
        List<ScoreEntry> scoreEntries;
        switch (event.getEventType()){
            case PullRoundRobin:
                //pull participants are individual players. game participants are teams
                Set<Participant> teams = new HashSet<>();
                for (Game game: eventGames){
                    teams.addAll(game.getParticipants());
                }
                List<ScoreEntry> teamScores = rankingUtil.getScores(teams, eventGames);
                Map<Player, ScoreEntry> playerScores = new HashMap<>();
                for (ScoreEntry teamScore: teamScores){
                    Team team = (Team) teamScore.getParticipant();
                    for (Player player: team.getPlayers()){
                        ScoreEntry playerScore = playerScores.get(player);
                        if (playerScore == null){
                            playerScore = new ScoreEntry();
                            playerScore.setParticipant(player);
                        } 
                        playerScore.setGamesPlayed(playerScore.getGamesPlayed()+teamScore.getGamesPlayed());
                        playerScore.setGamesWon(playerScore.getGamesWon()+teamScore.getGamesWon());
                        playerScore.setMatchesPlayed(playerScore.getMatchesPlayed()+teamScore.getMatchesPlayed());
                        playerScore.setMatchesWon(playerScore.getMatchesWon()+teamScore.getMatchesWon());
                        playerScore.setSetsPlayed(playerScore.getSetsPlayed()+teamScore.getSetsPlayed());
                        playerScore.setSetsWon(playerScore.getSetsWon()+teamScore.getSetsWon());
                        playerScore.setTotalPoints(playerScore.getTotalPoints()+teamScore.getTotalPoints());
                        playerScores.put(player, playerScore);
                    }
                }
                scoreEntries = new ArrayList<>(playerScores.values());
                Collections.sort(scoreEntries);
                break;
            default:
                scoreEntries = rankingUtil.getScores(event.getParticipants(), eventGames);
        }
        
        ModelAndView mav = new ModelAndView("scores/event", "Model", event);
        mav.addObject("ScoreEntries", scoreEntries);
        return mav;
    }
}
