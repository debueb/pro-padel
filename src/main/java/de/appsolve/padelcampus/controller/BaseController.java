/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller;

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
}
