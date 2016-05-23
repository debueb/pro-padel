/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import static de.appsolve.padelcampus.constants.Constants.FIRST_SET;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.db.model.GameSet;
import de.appsolve.padelcampus.db.model.Participant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Component
public class GameUtil {

    public void addGameResultMap(ModelAndView mav, Game game) {
        mav.addObject("GameResultMap", getGameResultMap(game, null));
    }
    
    public void addGameResultMap(ModelAndView mav, Collection<Game> games) {
        Map<Game, String> map = new HashMap<>();
        for (Game game : games) {
            map.put(game, getGameResultMap(game, null));
        }
        mav.addObject("GameResultMap", map);
    }

    public String getGameResultMap(Game game, final Participant sortByParticipant) {
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
                            return -1;
                        }
                        return 1;
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
    
}
