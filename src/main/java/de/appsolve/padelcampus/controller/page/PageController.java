/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.page;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.dao.ContactDAOI;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.dao.PageEntryDAOI;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.utils.Msg;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/page/{moduleId}")
public class PageController extends BaseController{
    
    private static final Logger log = Logger.getLogger(PageController.class);
    
    @Autowired
    ModuleDAOI moduleDAO;
    
    @Autowired
    PageEntryDAOI pageEntryDAO;
    
    @Autowired
    Validator validator;
    
    @Autowired
    ContactDAOI contactDAO;
    
    @Autowired
    Msg msg;
    
    @RequestMapping()
    public ModelAndView getIndex(@PathVariable("moduleId") String moduleTitle){
        return getIndexView(getModule(moduleTitle), new Mail());
    }
    
    @RequestMapping(method=POST)
    public ModelAndView postIndex(@PathVariable("moduleId") String moduleTitle, @ModelAttribute("Mail") Mail mail, BindingResult bindingResult){
        Module module = getModule(moduleTitle);
        ModelAndView defaultView = getIndexView(module, mail);
        return sendMail(defaultView, mail, bindingResult);
    }

    private ModelAndView getIndexView(Module module, Mail mail) {
        ModelAndView mav = new ModelAndView("page/index");
        mav.addObject("PageEntries", pageEntryDAO.findByModule(module));
        mav.addObject("Module", module);
        mav.addObject("Mail", mail);
        return mav;
    }

    private Module getModule(String moduleTitle) {
        Module module;
        try {
            Long id = Long.parseLong(moduleTitle);
            module = moduleDAO.findById(id);
        } catch (NumberFormatException e){
            module = moduleDAO.findByTitle(moduleTitle);
        }
        return module;
    }
}
