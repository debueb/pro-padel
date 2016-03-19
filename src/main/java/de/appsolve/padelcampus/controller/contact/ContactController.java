/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.contact;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.Mail;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/contact")
public class ContactController extends BaseController{
    
    private static final Logger log = Logger.getLogger(ContactController.class);
    
    @RequestMapping()
    public ModelAndView getIndex(){
        return getIndexView(new Mail());
    }
    
    @RequestMapping(method=POST)
    public ModelAndView postIndex(@ModelAttribute("Model") Mail mail, BindingResult bindingResult){
        ModelAndView defaultView = getIndexView(mail);
        return sendMail(defaultView, mail, bindingResult);
    }

    private ModelAndView getIndexView(Mail mail) {
        return new ModelAndView("contact/index", "Model", mail);
    }
}
