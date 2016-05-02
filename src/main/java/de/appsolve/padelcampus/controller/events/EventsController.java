/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.events;

import de.appsolve.padelcampus.constants.EventType;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.utils.EventsUtil;
import de.appsolve.padelcampus.utils.GameUtil;
import java.util.List;
import java.util.SortedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    
    @Autowired
    EventDAOI eventDAO;
    
    @Autowired
    EventsUtil eventsUtil;
    
    @Autowired
    GameUtil gameUtil;
    
    @RequestMapping()
    public ModelAndView getAll(@RequestParam(value="eventType", required = false) EventType eventType){
        List<Event> events;
        if (eventType == null){
            events = eventDAO.findAllActive();
        } else {
            events = eventDAO.findAllActiveWithEventType(eventType);
        }
        if (events.size()==1){
            return new ModelAndView("redirect:/events/"+events.get(0).getId());
        }
        return new ModelAndView("events/index", "Models", events);
    }
    
    @RequestMapping("{eventId}")
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
    
    @RequestMapping("{eventId}/groupgames")
    public ModelAndView getEventGroupGames(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findByIdFetchWithGames(eventId);
        ModelAndView mav = new ModelAndView("events/groupknockout/groupgames", "Model", event);
        gameUtil.addGameResultMap(mav, event.getGames());
        return mav;
    }
    
    @RequestMapping(method=GET, value="{eventId}/groupgamesend")
    public ModelAndView getEventGroupGamesEnd(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findById(eventId);
        ModelAndView mav = new ModelAndView("events/groupknockout/groupgamesend", "Model", event);
        return mav;
    }
    
    @RequestMapping(method=POST, value="{eventId}/groupgamesend")
    public ModelAndView saveEventGroupGamesEnd(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findById(eventId);
        return new ModelAndView("redirect:/events/"+eventId+"/knockoutgames");
    }
    
    @RequestMapping("{eventId}/knockoutgames")
    public ModelAndView getEventKnockoutGames(@PathVariable("eventId") Long eventId){
        Event event = eventDAO.findById(eventId);
        ModelAndView mav = new ModelAndView("events/groupknockout/knockoutgames", "Model", event);
        return mav;
    }
}
