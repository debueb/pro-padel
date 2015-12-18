/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.page;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import static de.appsolve.padelcampus.constants.Constants.CONTACT_FORM_RECIPIENT_MAIL;
import static de.appsolve.padelcampus.constants.Constants.CONTACT_FORM_RECIPIENT_NAME;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.EmailContact;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.dao.ContactDAOI;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.dao.PageEntryDAOI;
import de.appsolve.padelcampus.db.model.Contact;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.utils.MailUtils;
import de.appsolve.padelcampus.utils.Msg;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
    public ModelAndView getIndex(@PathVariable("moduleId") Long moduleId){
        return getIndexView(moduleId, new Mail());
    }
    
    @RequestMapping(method=POST)
    public ModelAndView postIndex(@PathVariable("moduleId") Long moduleId, @ModelAttribute("Mail") Mail mail, BindingResult bindingResult){
        ModelAndView defaultView = getIndexView(moduleId, mail);
        return sendMail(defaultView, mail, bindingResult);
    }

    private ModelAndView getIndexView(Long moduleId, Mail mail) {
        Module module = moduleDAO.findById(moduleId);
        ModelAndView mav = new ModelAndView("page/index");
        mav.addObject("PageEntries", pageEntryDAO.findByModule(module));
        mav.addObject("Module", module);
        mav.addObject("Mail", mail);
        return mav;
    }
}
