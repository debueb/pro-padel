/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller
public abstract class BaseController {
    
    private static final Logger log = Logger.getLogger(BaseController.class);
    
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
}
