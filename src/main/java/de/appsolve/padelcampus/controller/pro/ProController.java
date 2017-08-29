/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.pro;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dominik
 */
@Controller
@RequestMapping("/pro")
public class ProController {

    @RequestMapping
    public ModelAndView index(HttpServletRequest request) {
        return new ModelAndView("pro/index");
    }
}
