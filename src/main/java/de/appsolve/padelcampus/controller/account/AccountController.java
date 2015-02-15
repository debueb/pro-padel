/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.account;

import de.appsolve.padelcampus.controller.BaseController;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/account")
public class AccountController extends BaseController {
    
    @RequestMapping()
    public ModelAndView getIndex(HttpServletRequest request){
        return new ModelAndView("account/index");
    }
}
