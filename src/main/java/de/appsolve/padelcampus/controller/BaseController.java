/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import static de.appsolve.padelcampus.constants.Constants.CONTACT_FORM_RECIPIENT_MAIL;
import static de.appsolve.padelcampus.constants.Constants.CONTACT_FORM_RECIPIENT_NAME;
import de.appsolve.padelcampus.data.EmailContact;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.dao.ContactDAOI;
import de.appsolve.padelcampus.db.model.Contact;
import de.appsolve.padelcampus.utils.MailUtils;
import de.appsolve.padelcampus.utils.Msg;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller
public abstract class BaseController {
    
    private static final Logger log = Logger.getLogger(BaseController.class);
    
    @Autowired
    Validator validator;
    
    @Autowired
    ContactDAOI contactDAO;
    
    @Autowired
    Msg msg;
    
    @ExceptionHandler(value=Exception.class)
    public ModelAndView handleException(Exception ex){
        log.error(ex.getMessage(), ex);
        return new ModelAndView("error/500", "Exception", ex);
    }
    
    protected ModelAndView getLoginRequiredView(HttpServletRequest request, String title) {
        return getLoginRequiredView(title, request.getRequestURI());
    }
    
    private ModelAndView getLoginRequiredView(String title, String redirectUrl) {
        ModelAndView loginRequiredView = new ModelAndView("include/loginrequired");
        loginRequiredView.addObject("title", title);
        loginRequiredView.addObject("redirectURL", redirectUrl);
        return loginRequiredView;
    }
    
    public ModelAndView sendMail(ModelAndView defaultView, @ModelAttribute("Model") Mail mail, BindingResult bindingResult){
        validator.validate(mail, bindingResult);
        if (bindingResult.hasErrors()){
            return defaultView;
        }
        ModelAndView indexView = new ModelAndView("contact/index", "Model", mail);
        try {
            List<Contact> contacts = contactDAO.findAll();
            if (contacts.isEmpty()){
                Contact contact = new Contact();
                contact.setEmailAddress(CONTACT_FORM_RECIPIENT_MAIL);
                contact.setEmailDisplayName(CONTACT_FORM_RECIPIENT_NAME);
                contacts.add(contact);
            }
            mail.setRecipients(new ArrayList<EmailContact>(contacts));
            mail.setSubject("[Feedback] "+mail.getSubject());
            MailUtils.send(mail);
            return new ModelAndView("contact/success");  
        } catch (MandrillApiError | IOException e){
            log.error("Error while sending contact email", e);
            bindingResult.addError(new ObjectError("from", e.toString()));
            return indexView;
        }
    }
}
