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
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.utils.EventsUtil;
import de.appsolve.padelcampus.utils.GameUtil;
import de.appsolve.padelcampus.utils.RankingUtil;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
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
}
