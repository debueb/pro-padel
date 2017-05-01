/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller;

import com.google.common.collect.Sets;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.dao.ContactDAOI;
import de.appsolve.padelcampus.db.model.Contact;
import de.appsolve.padelcampus.exceptions.MailException;
import de.appsolve.padelcampus.exceptions.ResourceNotFoundException;
import de.appsolve.padelcampus.reporting.ErrorReporter;
import de.appsolve.padelcampus.utils.MailUtils;
import de.appsolve.padelcampus.utils.Msg;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller
public abstract class BaseController {
    
    private static final Logger LOG = Logger.getLogger(BaseController.class);
    
    @Autowired
    Validator validator;
    
    @Autowired
    ContactDAOI contactDAO;
    
    @Autowired
    public Msg msg;
    
    @Autowired
    public MailUtils mailUtils;
    
    @Autowired
    Environment environment;
    
    @Autowired
    protected ErrorReporter errorReporter;
    
    @Autowired
    private SessionUtil sessionUtil;
    
    @ExceptionHandler(value=Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(HttpServletRequest req, Exception ex){
        errorReporter.notify(ex);
        LOG.error(ex.getMessage(), ex);
        return new ModelAndView("error/500", "Exception", ex);
    }
    
    @ExceptionHandler(value=ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleResourceNotFoundException(HttpServletRequest request, Exception ex){
        LOG.error(ex.getMessage() + " " + request.getRequestURL());
        return new ModelAndView("error/404", "Exception", ex);
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
    
    public ModelAndView sendMail(HttpServletRequest request, ModelAndView defaultView, @ModelAttribute("Model") Mail mail, BindingResult bindingResult){
        validator.validate(mail, bindingResult);
        if (bindingResult.hasErrors()){
            return defaultView;
        }
        try {
            if (mail.getRecipients().isEmpty()){
                List<Contact> contacts = contactDAO.findAllForContactForm();
                if (contacts.isEmpty()){
                    contacts.add(getDefaultContact());
                }
                mail.setRecipients(Sets.newHashSet(contacts));
            }
            mail.setSubject("[Feedback] "+mail.getSubject());
            mailUtils.send(mail, request);
            ModelAndView mav = new ModelAndView("contact/success");
            mav.addObject("path", getPath());
            return mav;
        } catch (MailException | IOException e){
            LOG.error("Error while sending contact email", e);
            bindingResult.addError(new ObjectError("from", e.toString()));
            return defaultView;
        }
    }
    
    public String getPath() {
        return "";
    }

    protected Contact getDefaultContact() {
        Contact contact = new Contact();
        contact.setEmailAddress(environment.getProperty("DEFAULT_EMAIL_ADDRESS"));
        return contact;
    }
    
    protected ModelAndView getLoginView(HttpServletRequest request) {
        String redirectPath = request.getParameter("redirectUrl");
        if (redirectPath == null){
            redirectPath = request.getRequestURL().toString();
        }
        sessionUtil.setLoginRedirectPath(request, redirectPath);
        return new ModelAndView("redirect:/login");
    }
}
