/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.db.dao.FooterLinkDAOI;
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
@RequestMapping("/footer")
public class FooterController extends BaseController{
    
    @Autowired
    FooterLinkDAOI footerLinkDAO;
    
    @RequestMapping("{id}")
    public ModelAndView getIndex(@PathVariable("id") Long id){
        return new ModelAndView("footer/index", "FooterLink", footerLinkDAO.findById(id));
    }
}
