/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import static de.appsolve.padelcampus.constants.Constants.COOKIE_LOGIN_TOKEN;
import de.appsolve.padelcampus.utils.SessionUtil;
import javax.servlet.http.Cookie;
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
    
    @RequestMapping()
    public ModelAndView showLogin(HttpServletRequest request, HttpServletResponse response){
        sessionUtil.invalidate(request);
        Cookie cookie = new Cookie(COOKIE_LOGIN_TOKEN, null);
        if (!request.getServerName().equals("localhost")){
            cookie.setDomain(request.getServerName());
        }
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return new ModelAndView("redirect:/");
    }
}
