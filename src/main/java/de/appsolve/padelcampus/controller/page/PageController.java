/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.page;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.dao.PageEntryDAOI;
import de.appsolve.padelcampus.db.model.Module;
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
@RequestMapping("/page/{moduleId}")
public class PageController extends BaseController{
    
    @Autowired
    ModuleDAOI moduleDAO;
    
    @Autowired
    PageEntryDAOI pageEntryDAO;
    
    @RequestMapping()
    public ModelAndView getIndex(@PathVariable("moduleId") Long moduleId){
        Module module = moduleDAO.findById(moduleId);
        ModelAndView mav = new ModelAndView("page/index");
        mav.addObject("PageEntries", pageEntryDAO.findByModule(module));
        mav.addObject("Module", module);
        return mav;
    }
}
