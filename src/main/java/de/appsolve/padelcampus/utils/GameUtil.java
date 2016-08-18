/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import static de.appsolve.padelcampus.constants.Constants.FIRST_SET;
import de.appsolve.padelcampus.db.dao.GameDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.db.model.GameSet;
import de.appsolve.padelcampus.db.model.Participant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Component
public class GameUtil {
    
    @Autowired
    GameDAOI gameDAO;        

    public void addGameResultMap(ModelAndView mav, Game game) {
        Map<Game, String> map = new HashMap<>();
        map.put(game, getGameResult(game, null, false));
        mav.addObject("GameResultMap", map);
    }
    
    public void addGameResultMap(ModelAndView mav, Collection<Game> games) {
        Map<Game, String> map = new HashMap<>();
        for (Game game : games) {
            map.put(game, getGameResult(game, null, false));
        }
        mav.addObject("GameResultMap", map);
    }

    public String getGameResult(Game game, final Participant sortByParticipant, final Boolean reverseGameResult) {
        StringBuilder result = new StringBuilder();

        List<GameSet> gameSets = new ArrayList<>(game.getGameSets());
        Collections.sort(gameSets);

        int gameSetsDisplayed=0;
        for (int set=FIRST_SET; set<gameSets.size(); set++){
            int participantId = 0;
            Set<Participant> participants = game.getParticipants();
            if (sortByParticipant != null){
                SortedSet<Participant> sortedParticipants = new TreeSet<>(new Comparator<Participant>() {
                    @Override
                    public int compare(Participant o1, Participant o2) {
                        if (o1.equals(o2)){
                            return 0;
                        }
                        if (o1.equals(sortByParticipant)){
                            return reverseGameResult ? 1 : -1;
                        }
                        return reverseGameResult ? -1 : 1;
                    }
                });
                sortedParticipants.addAll(participants);
                participants = sortedParticipants;
            }
            
            for (Participant participant: participants){
                String setGames = "-";
                for (GameSet gs: gameSets){
                    if (gs.getSetNumber() == set && gs.getParticipant().equals(participant)){
                        setGames = gs.getSetGames()+"";
                        gameSetsDisplayed++;
                        break;
                    }
                }
                result.append(setGames);
                result.append(participantId%2==0 ? ":" : " "); //separate games by colon, sets by space
                participantId++;
            }
            if (gameSetsDisplayed == gameSets.size()){
                break; //do not display third set if it was not played
            }
        }
        
        return result.toString();
    }
    
    public void createMissingGames(Event event, Collection<Game> existingGames, Set<Participant> participants) {
        createMissingGames(event, existingGames, participants, null);
    }
    
    public void createMissingGames(Event event, Collection<Game> existingGames, Set<Participant> participants, Integer groupNumber) {
        for (Participant firstParticipant: participants){
            for (Participant secondParticipant: participants){
                if (!firstParticipant.equals(secondParticipant)){
                    boolean gameExists = false;
                    for (Game game: existingGames){
                        if (game.getParticipants().contains(firstParticipant) && game.getParticipants().contains(secondParticipant)){
                            gameExists = true;
                            break;
                        }
                    }
                    if (!gameExists){
                        Game game = new Game();
                        game.setEvent(event);
                        if (groupNumber != null){
                            game.setGroupNumber(groupNumber);
                        }
                        Set<Participant> gameParticipants = new LinkedHashSet<>();
                        gameParticipants.add(firstParticipant);
                        gameParticipants.add(secondParticipant);
                        game.setParticipants(gameParticipants);
                        gameDAO.saveOrUpdate(game);
                        existingGames.add(game);
                    }
                }
            }
        }
    }
    
    public Map<Participant, Map<Game, String>> getParticipantGameResultMap(Collection<Game> games, Boolean reverseGameResult) {
        Map<Participant, Map<Game, String>> participantGameResultMap = new HashMap<>();
        for (Game game: games){
            for (Participant p: game.getParticipants()){
                Map<Game, String> gameResultMap = participantGameResultMap.get(p);
                if (gameResultMap == null){
                    gameResultMap = new HashMap<>();
                }
                String result = getGameResult(game, p, reverseGameResult);
                gameResultMap.put(game, result);
                participantGameResultMap.put(p, gameResultMap);
            }
        }
        return participantGameResultMap;
    }
}
