/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.dao.ContactDAOI;
import de.appsolve.padelcampus.db.model.Contact;
import de.appsolve.padelcampus.exceptions.MailException;
import de.appsolve.padelcampus.exceptions.ResourceNotFoundException;
import de.appsolve.padelcampus.utils.MailUtils;
import de.appsolve.padelcampus.utils.Msg;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.exception.ExceptionUtils;
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
    
    private static final Logger log = Logger.getLogger(BaseController.class);
    
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
    
    @ExceptionHandler(value=Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(HttpServletRequest req, Exception ex){
        sendErrorMail(req, ex);
        log.error(ex.getMessage(), ex);
        return new ModelAndView("error/500", "Exception", ex);
    }
    
    @ExceptionHandler(value=ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleResourceNotFoundException(Exception ex){
        log.error(ex.getMessage(), ex);
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
                mail.setRecipients(contacts);
            }
            mail.setSubject("[Feedback] "+mail.getSubject());
            mailUtils.send(mail, request);
            ModelAndView mav = new ModelAndView("contact/success");
            mav.addObject("path", getPath());
            return mav;
        } catch (MailException | IOException e){
            log.error("Error while sending contact email", e);
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
    
    protected void sendErrorMail(HttpServletRequest request, Exception ex) {
        boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("jdwp");
        if (!isDebug){
            StringBuilder body = new StringBuilder();
            body.append("METHOD URL:\n");
            body.append(request.getMethod());
            body.append(" ");
            body.append(request.getRequestURL());
            body.append("\n\nREQUEST HEADERS\n");
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null){
                while (headerNames.hasMoreElements()){
                    String attr = headerNames.nextElement();
                    body.append(attr);
                    body.append("=");
                    body.append(request.getHeader(attr));
                    body.append("\n");
                }
            }
            body.append("\n\nREQUEST PARAMETERS\n");
            body.append(request.getParameterMap());
            body.append("\n\nSESSION ATTRIBUTES\n");
            Enumeration<String> attributeNames = request.getSession().getAttributeNames();
            if (attributeNames != null){
                while (attributeNames.hasMoreElements()){
                    String attr = attributeNames.nextElement();
                    body.append(attr);
                    body.append("=");
                    body.append(request.getSession().getAttribute(attr));
                    body.append("\n");
                }
            }
            body.append("\n\nEXCEPTION MESSAGE\n");
            body.append(ex.getMessage());
            body.append("\n\nEXCEPTION STACKTRACE\n");
            body.append(ExceptionUtils.getStackTrace(ex));
            Mail mail = new Mail();
            mail.addRecipient(getDefaultContact());
            mail.setSubject("pro padel error - "+ex.toString());
            mail.setBody(body.toString());
            try {
                mailUtils.send(mail, request);
            } catch (MailException | IOException e){
                log.error(e.getMessage(), e);
            }
        }
    }
}
