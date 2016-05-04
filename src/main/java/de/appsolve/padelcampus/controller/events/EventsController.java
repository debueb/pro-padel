/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.events;

import de.appsolve.padelcampus.constants.EventType;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.ScoreEntry;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.utils.EventsUtil;
import de.appsolve.padelcampus.utils.GameUtil;
import de.appsolve.padelcampus.utils.RankingUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/events")
public class EventsController extends BaseController{
    
    private static final Integer NUMBER_OF_PARTICIPANTS_TO_PROCEED_TO_KNOCKOUT_GAMES = 2;
    
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
        List<Event> events = eventDAO.findAll();
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
                mav.addObject("RoundGameMap", roundGames);
                break;
        }
        
        return mav;
    }
    
    @RequestMapping("event/{eventId}/groupgames")
    public ModelAndView getEventGroupGames(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        ModelAndView mav = new ModelAndView("events/groupknockout/groupgames", "Model", event);
        
        
        event = eventDAO.findByIdFetchWithGames(eventId);
        SortedMap<Integer, List<Game>> groupGameMap = eventsUtil.getGroupGames(event);
        SortedMap<Integer, List<Game>> roundGameMap = eventsUtil.getRoundGames(event);
        mav.addObject("GroupGameMap", groupGameMap);
        mav.addObject("RoundGameMap", roundGameMap);
        
        gameUtil.addGameResultMap(mav, event.getGames());
        return mav;
    }
    
    @RequestMapping(method=GET, value="event/{eventId}/groupgamesend")
    public ModelAndView getEventGroupGamesEnd(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findById(eventId);
        return getGroupGamesEndView(event);
    }
    
    @RequestMapping(method=POST, value="event/{eventId}/groupgamesend")
    public ModelAndView saveEventGroupGamesEnd(@PathVariable("eventId") Long eventId, @ModelAttribute("Model") Event dummy, BindingResult result){
        Event event = eventDAO.findByIdFetchWithParticipantsAndGames(eventId);
        
        SortedMap<Integer, List<Game>> roundGames = eventsUtil.getRoundGames(event);
        if (!roundGames.isEmpty()){
            result.reject("GroupGamesAlreadyEnded");
            return getGroupGamesEndView(dummy);
        }
        
        SortedMap<Integer, List<Game>> groupGames = eventsUtil.getGroupGames(event);
        Iterator<Map.Entry<Integer, List<Game>>> iterator = groupGames.entrySet().iterator();
        
        //determine best participants of each group
        Map<Integer, List<Participant>> rankedGroupParticipants = new TreeMap<>();
        while (iterator.hasNext()){
            Map.Entry<Integer, List<Game>> entry = iterator.next();
            Integer groupNumber = entry.getKey();
            List<Game> games = entry.getValue();
            
            //determine participant based on games to filter out participants who did not play
            Set<Participant> participants = new HashSet<>();
            List<Game> playedGames = new ArrayList<>();
            for (Game game: games){
                if (!game.getGameSets().isEmpty()){
                    participants.addAll(game.getParticipants());
                    playedGames.add(game);
                }
            }
            
            if (participants.isEmpty() || playedGames.isEmpty()){
                result.reject("CannotEndGroupGames");
                return getGroupGamesEndView(dummy);
            }
            
            //get list of score entries sorted by rank
            List<ScoreEntry> scoreEntries =  rankingUtil.getScores(participants, playedGames);
            for (int groupPos=0; groupPos<NUMBER_OF_PARTICIPANTS_TO_PROCEED_TO_KNOCKOUT_GAMES; groupPos++){
                List<Participant> rankedParticipants = rankedGroupParticipants.get(groupNumber);
                if (rankedParticipants == null){
                    rankedParticipants = new ArrayList<>();
                }
                Participant p = null;
                try {
                    p = scoreEntries.get(groupPos).getParticipant();
                } catch (IndexOutOfBoundsException e){
                    //could happen when not enough games were played in one group
                }
                rankedParticipants.add(p);
                rankedGroupParticipants.put(groupNumber, rankedParticipants);
            }
        }
        
        //sort participants so that group winners are first
        List<Participant> rankedParticipants = new ArrayList<>();
        for (int groupPos=0; groupPos<NUMBER_OF_PARTICIPANTS_TO_PROCEED_TO_KNOCKOUT_GAMES; groupPos++){
            for (int group=0; group<event.getNumberOfGroups(); group++){
                rankedParticipants.add(rankedGroupParticipants.get(group).get(groupPos));
            }
        }
        
        eventsUtil.createKnockoutGames(event, rankedParticipants);
        
        return new ModelAndView("redirect:/events/event/"+eventId+"/knockoutgames");
    }
    
    @RequestMapping("event/{eventId}/knockoutgames")
    public ModelAndView getEventKnockoutGames(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        ModelAndView mav = new ModelAndView("events/knockout", "Model", event);
        SortedMap<Integer, List<Game>> groupGameMap = eventsUtil.getGroupGames(event);
        SortedMap<Integer, List<Game>> roundGameMap = eventsUtil.getRoundGames(event);
        if (roundGameMap.isEmpty()){
            return new ModelAndView("events/groupknockout/knockoutgames", "Model", event);
        }
        
        mav.addObject("GroupGameMap", groupGameMap);
        mav.addObject("RoundGameMap", roundGameMap);
        return mav;
    }

    private ModelAndView getGroupGamesEndView(Event event) {
        ModelAndView mav = new ModelAndView("events/groupknockout/groupgamesend", "Model", event);
        return mav;
    }
}
