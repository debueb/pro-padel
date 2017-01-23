/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.admin.controller.mail;

import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.exceptions.MailException;
import de.appsolve.padelcampus.spring.PlayerCollectionEditor;
import de.appsolve.padelcampus.utils.MailUtils;
import java.io.IOException;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller
@RequestMapping("/admin/mail")
public class AdminMailController{
    
    @Autowired
    PlayerCollectionEditor playerCollectionEditor;
    
    @Autowired
    MailUtils mailUtils;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Set.class, "recipients", playerCollectionEditor);
    }
    
    @RequestMapping(method=POST)
    public ModelAndView postMailAll(HttpServletRequest request, @ModelAttribute("Model") Mail mail, BindingResult result){
        if (result.hasErrors()){
            return getMailView(mail);
        }
        try {
            mailUtils.send(mail, request);
            return new ModelAndView("admin/mail-success");
        } catch (IOException | MailException e){
            result.addError(new ObjectError("*", e.getMessage()));
            return getMailView(mail);
        }
    }
    
    private ModelAndView getMailView(Mail mail) {
        return new ModelAndView("admin/mail", "Model", mail);
    }
}
