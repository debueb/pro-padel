/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.links;

import de.appsolve.padelcampus.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/links")
public class LinksController extends BaseController{
    
    @RequestMapping()
    public ModelAndView getIndex(){
        return new ModelAndView("links/index");
    }
}
