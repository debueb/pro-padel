/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.events;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.ScoreEntry;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.model.Community;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.db.model.GameSet;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Team;
import de.appsolve.padelcampus.exceptions.ResourceNotFoundException;
import de.appsolve.padelcampus.utils.EventsUtil;
import de.appsolve.padelcampus.utils.GameUtil;
import de.appsolve.padelcampus.utils.RankingUtil;
import de.appsolve.padelcampus.utils.SortUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
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
        Module module = moduleDAO.findByUrlTitle(moduleTitle);
        if (module == null){
            throw new ResourceNotFoundException();
        }
        List<Event> events = eventDAO.findAllActive();
        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()){
            Event event = iterator.next();
            if (event.getEventGroup() == null || !module.getEventGroups().contains(event.getEventGroup())){
                iterator.remove();
            }
        }
        ModelAndView mav = new ModelAndView("events/index", "Models", events);
        mav.addObject("Module", module);
        return mav;
    }
    
    @RequestMapping("event/{eventId}")
    public ModelAndView getEvent(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findById(eventId);
        if (event == null){
            throw new ResourceNotFoundException();
        }
        ModelAndView mav = new ModelAndView("events/event", "Model", event);
        return mav;
    }
    
    @RequestMapping("event/{eventId}/participants")
    public ModelAndView getEventParticipants(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        ModelAndView mav = new ModelAndView("events/participants", "Model", event);
        return mav;
    }
    
    @RequestMapping("event/{eventId}/communities")
    public ModelAndView getEventCommunities(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        SortedMap<Participant, BigDecimal> rankedParticipants = rankingUtil.getRankedParticipants(event);
        Map<Community, SortedMap<Participant, BigDecimal>> communityMap = new HashMap<>();
        for (Participant participant: rankedParticipants.keySet()){
            if (participant instanceof Team){
                Team team = (Team) participant;
                if (team.getCommunity() != null){
                    SortedMap<Participant, BigDecimal> communityParticipantMap = communityMap.get(team.getCommunity());
                    if (communityParticipantMap == null){
                        communityParticipantMap = new TreeMap<>();
                    }
                    communityParticipantMap.put(team, rankedParticipants.get(team));
                    SortedMap<Participant, BigDecimal> sortedMap = SortUtil.sortMap(communityParticipantMap);
                    communityMap.put(team.getCommunity(), sortedMap);
                }
            }
        }
        ModelAndView mav = new ModelAndView("events/communityroundrobin/communities", "Model", event);
        mav.addObject("CommunityMap", communityMap);
        return mav;
    }
    
    @RequestMapping("event/{eventId}/communitygames")
    public ModelAndView getEventCommunityGames(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        ModelAndView mav = new ModelAndView("events/communityroundrobin/communitygames", "Model", event);
        
        //Community // Participant // Game // GameResult
        SortedMap<Community, Map<Participant, Map<Game, String>>> communityParticipantGameResultMap = new TreeMap<>();
        
        Map<Participant, Map<Game, String>> participantGameResultMap = gameUtil.getParticipantGameResultMap(event.getGames(), false);
        for (Entry<Participant,Map<Game,String>> entry: participantGameResultMap.entrySet()){
            Participant p = entry.getKey();
            if (p instanceof Team){
                Team team = (Team) p;
                Map<Participant, Map<Game, String>> participantMap = communityParticipantGameResultMap.get(team.getCommunity());
                if (participantMap == null){
                    participantMap = new HashMap<>();
                }
                participantMap.put(p, entry.getValue());
                communityParticipantGameResultMap.put(team.getCommunity(), participantMap);
            }
        }
        mav.addObject("GroupParticipantGameResultMap", communityParticipantGameResultMap);
        gameUtil.addGameResultMap(mav, event.getGames());
        return mav;
    }
    
    @RequestMapping("event/{eventId}/pullgames")
    public ModelAndView getEventPullGames(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        ModelAndView mav = new ModelAndView("events/pullroundrobin/pullgames", "Model", event);
        gameUtil.addGameResultMap(mav, event.getGames());
        return mav;
    }
    
    @RequestMapping("event/{eventId}/groupgames")
    public ModelAndView getEventGroupGames(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        ModelAndView mav = new ModelAndView("events/groupknockout/groupgames", "Model", event);
        
        event = eventDAO.findByIdFetchWithGames(eventId);
        SortedMap<Integer, List<Game>> groupGameMap = eventsUtil.getGroupGameMap(event);
        SortedMap<Integer, List<Game>> roundGameMap = eventsUtil.getRoundGameMap(event);
        
        //Group // Participant // Game // GameResult
        SortedMap<Integer, Map<Participant, Map<Game, String>>> groupParticipantGameResultMap = new TreeMap<>();
        
        Iterator<Map.Entry<Integer, List<Game>>> iterator = groupGameMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer, List<Game>> entry = iterator.next();
            Map<Participant, Map<Game, String>> participantGameResultMap = gameUtil.getParticipantGameResultMap(entry.getValue(), false);
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
        SortedMap<Integer, List<Game>> groupGameMap = eventsUtil.getGroupGameMap(event);
        SortedMap<Integer, List<Game>> roundGameMap = eventsUtil.getRoundGameMap(event);
        if (roundGameMap.isEmpty()){
            return new ModelAndView("events/groupknockout/knockoutgamesend", "Model", event);
        }
        ModelAndView mav = getKnockoutView(event, roundGameMap);
        mav.addObject("GroupGameMap", groupGameMap);
        return mav;
    }
    
    @RequestMapping(method=GET, value="event/{eventId}/score")
    public ModelAndView getScore(@PathVariable("eventId") Long eventId, HttpServletRequest request){
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        SortedMap<Community, ScoreEntry> communityScoreMap = new TreeMap<>();
        List<ScoreEntry> scores = rankingUtil.getScores(event.getParticipants(), event.getGames());
        for (ScoreEntry scoreEntry: scores){
            Participant p = scoreEntry.getParticipant();
            if (p instanceof Team){
                Team team = (Team) p;
                ScoreEntry communityScore = communityScoreMap.get(team.getCommunity());
                if (communityScore == null){
                    communityScore = new ScoreEntry();
                }
                communityScore.add(scoreEntry);
                communityScoreMap.put(team.getCommunity(), communityScore);
            }
        }
        
        ModelAndView scoreView = new ModelAndView("events/communityroundrobin/score");
        scoreView.addObject("Model", event);
        scoreView.addObject("CommunityScoreMap", communityScoreMap);
        return scoreView;
    }
    
    private ModelAndView getKnockoutView(Event event, SortedMap<Integer, List<Game>> roundGameMap) {
        ModelAndView mav = new ModelAndView("events/knockout/knockoutgames");
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
