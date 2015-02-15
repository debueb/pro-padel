/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.app;

import de.appsolve.padelcampus.controller.BaseController;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller
@RequestMapping("app")
public class AppController extends BaseController {
    
    private static final Logger log = Logger.getLogger(AppController.class);
    
    @RequestMapping("ios")
    public ModelAndView getIOS(){
        return new ModelAndView("app/ios");
    }
    
    @RequestMapping("android")
    public ModelAndView getAndroid(){
        return new ModelAndView("app/android");
    }
}
