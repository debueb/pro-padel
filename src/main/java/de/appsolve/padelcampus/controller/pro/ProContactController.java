/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.pro;

import de.appsolve.padelcampus.controller.contact.ContactController;
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
@Controller
@RequestMapping("/pro/contact")
public class ProContactController extends ContactController {
    
    @Override
    public String getPath(){
       return "pro/";
    }    
   
    @RequestMapping(method=POST)
    @Override
    public ModelAndView postIndex(HttpServletRequest request, @ModelAttribute("Model") Mail mail, BindingResult bindingResult){
        ModelAndView defaultView = super.getIndexView(mail);
        mail.addRecipient(getDefaultContact());
        return sendMail(request, defaultView, mail, bindingResult);
    }
}
