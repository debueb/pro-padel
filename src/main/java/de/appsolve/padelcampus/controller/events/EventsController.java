/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.events;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.db.model.GameSet;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.utils.EventsUtil;
import de.appsolve.padelcampus.utils.GameUtil;
import de.appsolve.padelcampus.utils.RankingUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
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
@RequestMapping("/events")
public class EventsController extends BaseController{
    
    @Autowired
    ModuleDAOI moduleDAO;
    
    @Autowired
    EventDAOI eventDAO;
    
    @Autowired
    EventsUtil eventsUtil;
    
    @Autowired
    GameUtil gameUtil;
    
    @Autowired
    RankingUtil rankingUtil;
    
    @RequestMapping("{moduleTitle}")
    public ModelAndView getEvent(@PathVariable("moduleTitle") String moduleTitle){
        Module module = moduleDAO.findByTitle(moduleTitle);
        List<Event> events = eventDAO.findAllActive();
        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()){
            Event event = iterator.next();
            if (!module.getEventTypes().contains(event.getEventType())){
                iterator.remove();
            }
        }
        if (events.size()==1){
            return new ModelAndView("redirect:/events/event/"+events.get(0).getId());
        }
        return new ModelAndView("events/index", "Models", events);
    }
    
    @RequestMapping("event/{eventId}")
    public ModelAndView getEvent(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findById(eventId);
        String eventType = event.getEventType().toString().toLowerCase();
        ModelAndView mav = new ModelAndView("events/"+eventType, "Model", event);
        
        switch (event.getEventType()){
            case Knockout:
                event = eventDAO.findByIdFetchWithGames(eventId);
                SortedMap<Integer, List<Game>> roundGames = eventsUtil.getRoundGames(event);
                mav = getKnockoutView(event, roundGames);
                break;
        }
        
        return mav;
    }
    
    @RequestMapping("event/{eventId}/participants")
    public ModelAndView getEventParticipants(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        SortedMap<Participant, BigDecimal> rankedParticipants = rankingUtil.getRankedParticipants(event);
        ModelAndView mav = new ModelAndView("events/participants", "Model", event);
        mav.addObject("RankedParticipants", rankedParticipants);
        return mav;
    }
    
    @RequestMapping("event/{eventId}/groupgames")
    public ModelAndView getEventGroupGames(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        ModelAndView mav = new ModelAndView("events/groupknockout/groupgames", "Model", event);
        
        
        event = eventDAO.findByIdFetchWithGames(eventId);
        SortedMap<Integer, List<Game>> groupGameMap = eventsUtil.getGroupGames(event);
        SortedMap<Integer, List<Game>> roundGameMap = eventsUtil.getRoundGames(event);
        
        //Group // Participant // Game // GameResult
        SortedMap<Integer, Map<Participant, Map<Game, String>>> groupParticipantGameResultMap = new TreeMap<>();
        
        Iterator<Map.Entry<Integer, List<Game>>> iterator = groupGameMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer, List<Game>> entry = iterator.next();
            Map<Participant, Map<Game, String>> participantGameResultMap = new HashMap<>();
            for (Game game: entry.getValue()){
                for (Participant p: game.getParticipants()){
                    Map<Game, String> gameResultMap = participantGameResultMap.get(p);
                    if (gameResultMap == null){
                        gameResultMap = new HashMap<>();
                    }
                    String result = gameUtil.getGameResultMap(game, p);
                    gameResultMap.put(game, result);
                    participantGameResultMap.put(p, gameResultMap);
                }
            }
            Integer group = entry.getKey();
            groupParticipantGameResultMap.put(group, participantGameResultMap);
        }
        mav.addObject("GroupParticipantGameResultMap", groupParticipantGameResultMap);
        mav.addObject("RoundGameMap", roundGameMap);
        
        gameUtil.addGameResultMap(mav, event.getGames());
        return mav;
    }
    
    @RequestMapping("event/{eventId}/knockoutgames")
    public ModelAndView getEventKnockoutGames(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        SortedMap<Integer, List<Game>> groupGameMap = eventsUtil.getGroupGames(event);
        SortedMap<Integer, List<Game>> roundGameMap = eventsUtil.getRoundGames(event);
        if (roundGameMap.isEmpty()){
            return new ModelAndView("events/groupknockout/knockoutgames", "Model", event);
        }
        ModelAndView mav = getKnockoutView(event, roundGameMap);
        mav.addObject("GroupGameMap", groupGameMap);
        return mav;
    }

    private ModelAndView getKnockoutView(Event event, SortedMap<Integer, List<Game>> roundGameMap) {
        ModelAndView mav = new ModelAndView("events/knockout");
        mav.addObject("Model", event);
        mav.addObject("RoundGameMap", roundGameMap);
        mav.addObject("ParticipantGameGameSetMap", getParticipantGameGameSetMap(roundGameMap));
        return mav;
    }
    
    private Map<Participant, Map<Game, List<GameSet>>> getParticipantGameGameSetMap(SortedMap<Integer, List<Game>> roundGameMap) {
        Map<Participant, Map<Game, List<GameSet>>> participantGameGameSetMap = new HashMap<>();
        Iterator<List<Game>> gameList = roundGameMap.values().iterator();
        while(gameList.hasNext()){
            List<Game> games = gameList.next();
            for (Game game: games){
                for (Participant p: game.getParticipants()){
                    Map<Game, List<GameSet>> gameGameMap = participantGameGameSetMap.get(p);
                    if (gameGameMap == null){
                        gameGameMap = new HashMap<>();
                    }
                    Set<GameSet> gameSets = game.getGameSets();
                    List<GameSet> participantGameSets = new ArrayList<>();
                    for (GameSet gs: gameSets){
                        if (gs.getParticipant().equals(p)){
                            participantGameSets.add(gs);
                        }
                    }
                    Collections.sort(participantGameSets);
                    gameGameMap.put(game, participantGameSets);
                    participantGameGameSetMap.put(p, gameGameMap);
                }
            }
        }
        return participantGameGameSetMap;
    }

}
