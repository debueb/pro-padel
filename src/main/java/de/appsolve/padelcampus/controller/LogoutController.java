/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.utils.LoginUtil;
import de.appsolve.padelcampus.utils.SessionUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/logout")
public class LogoutController extends BaseController{
    
    @Autowired
    SessionUtil sessionUtil;
    
    @Autowired
    LoginUtil loginUtil;
    
    @RequestMapping()
    public ModelAndView doLogout(HttpServletRequest request, HttpServletResponse response){
        loginUtil.deleteLoginCookie(request, response);
        sessionUtil.invalidate(request);
        return new ModelAndView("redirect:/");
    }
}
