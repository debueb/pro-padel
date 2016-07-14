/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.dao.PageEntryDAOI;
import de.appsolve.padelcampus.db.model.PageEntry;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping(value = {"/", "index", "index.html"})
public class RootController extends BaseController{
    
    @Autowired
    PageEntryDAOI pageEntryDAO;
    
    @RequestMapping()
    public ModelAndView getIndex(){
        List<PageEntry> pageEntries = pageEntryDAO.findForHomePage();
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("PageEntries", pageEntries);
        mav.addObject("Mail", new Mail());
        return mav;
    }
}
