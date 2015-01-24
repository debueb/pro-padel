/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.db.dao.EventDAOI;
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
    EventDAOI eventDAO;
    
    @RequestMapping()
    public ModelAndView getAll(){
        return new ModelAndView("events/index", "Models", eventDAO.findAllActive());
    }
    
    @RequestMapping("{eventId}")
    public ModelAndView getEvent(@PathVariable("eventId") Long eventId){
        ModelAndView mav = new ModelAndView("events/event", "Model", eventDAO.findById(eventId));
        return mav;
    }
}
