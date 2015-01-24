/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/error")
public class ErrorController extends BaseController{
    
    @RequestMapping("/{errorCode}")
    public ModelAndView getErrorView(@PathVariable("errorCode") String errorCode){
        return new ModelAndView("error/"+errorCode);
    }
}
