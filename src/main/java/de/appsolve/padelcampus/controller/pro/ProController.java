/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.pro;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author dominik
 */
@Controller
@RequestMapping("/pro")
public class ProController {
    
    @RequestMapping
    public ModelAndView index(HttpServletRequest request){
        String serverName = request.getServerName();
        if (!StringUtils.isEmpty(serverName)){
            String[] domainParts = serverName.split("\\.");
            if (domainParts.length>2){
                String url = request.getScheme()+"://"+domainParts[domainParts.length-2]+"."+domainParts[domainParts.length-1];
                if (request.getScheme().equals("http") && request.getServerPort() != 80){
                    url += ":"+request.getServerPort();
                }
                url += request.getRequestURI();
                RedirectView rv = new RedirectView(url);
                rv.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
                return new ModelAndView(rv);
            }
        }
        return new ModelAndView("pro/index");
    }
}
