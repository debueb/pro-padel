/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Game;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class EventsUtil {

    public SortedMap<Integer, List<Game>> getRoundGames(Event event) {
        SortedMap<Integer, List<Game>> roundGames= new TreeMap<>();
        
        for (Game game: event.getGames()){
            List<Game> games = roundGames.get(game.getRound());
            if (games == null){
                games = new ArrayList<>();
            }
            games.add(game);
            roundGames.put(game.getRound(), games);
        }
        return roundGames;
    }
    
}
