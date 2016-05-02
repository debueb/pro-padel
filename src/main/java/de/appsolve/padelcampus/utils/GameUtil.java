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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Component
public class GameUtil {

    public void addGameResultMap(ModelAndView mav, Game game) {
        addGameResultMap(mav, Arrays.asList(new Game[]{game}));
    }
    
    public void addGameResultMap(ModelAndView mav, Collection<Game> games) {
        Map<Game, String> map = new HashMap<>();
        for (Game game : games) {
            List<GameSet> gameSets = new ArrayList<>(game.getGameSets());
            StringBuilder result = new StringBuilder();
            
            Collections.sort(gameSets);
            int gameSetsDisplayed=0;
            for (int set=FIRST_SET; set<gameSets.size(); set++){
                int participantId = 0;
                for (Participant participant: game.getParticipants()){
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
            map.put(game, result.toString());
        }
        mav.addObject("GameResultMap", map);
    }
    
}
