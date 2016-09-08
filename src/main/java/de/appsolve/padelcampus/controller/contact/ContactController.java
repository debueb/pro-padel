/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.contact;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.Mail;
import javax.servlet.http.HttpServletRequest;
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
    
    @RequestMapping()
    public ModelAndView getIndex(){
        return getIndexView(new Mail());
    }
    
    @RequestMapping(method=POST)
    public ModelAndView postIndex(HttpServletRequest request, @ModelAttribute("Model") Mail mail, BindingResult bindingResult){
        ModelAndView defaultView = getIndexView(mail);
        return sendMail(request, defaultView, mail, bindingResult);
    }

    protected ModelAndView getIndexView(Mail mail) {
        ModelAndView mav = new ModelAndView("contact/index", "Model", mail);
        mav.addObject("path", getPath());
        return mav;
    }
}
