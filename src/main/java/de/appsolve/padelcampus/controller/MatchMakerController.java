/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.db.dao.NewsDAOI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/matchmaker")
public class MatchMakerController extends BaseController{
    
    @Autowired
    NewsDAOI newsDAO;
    
    @RequestMapping()
    public ModelAndView getIndex(){
        return new ModelAndView("matchmaker/index");
    }
}
