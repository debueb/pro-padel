/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.pro;

import de.appsolve.padelcampus.constants.EventType;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.model.Event;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller
@RequestMapping("/pro/tournaments")
public class ProTournamentsController {
    
    @Autowired
    EventDAOI eventDAO;
    
    @RequestMapping
    public ModelAndView index(){
        List<Event> activeEvents = eventDAO.findAllActive();
        Iterator<Event> iterator = activeEvents.iterator();
        List<Event> upcomingEvents = new ArrayList<>();
        List<Event> pastEvents = new ArrayList<>();
        LocalDate today = new LocalDate();
        while (iterator.hasNext()){
            Event event = iterator.next();
            if (event.getEventType().equals(EventType.SingleRoundRobin)){
                iterator.remove();
            } else {
                if (event.getStartDate().isBefore(today)){
                    pastEvents.add(event);
                } else {
                    upcomingEvents.add(event);
                }
            }
        }
        ModelAndView mav = new ModelAndView("pro/tournaments");
        mav.addObject("PastEvents", pastEvents);
        mav.addObject("UpcomingEvents", upcomingEvents);
        return mav;
    }
}
