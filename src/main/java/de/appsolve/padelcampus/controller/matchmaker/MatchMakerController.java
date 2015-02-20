/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.matchmaker;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.MatchOfferDAOI;
import de.appsolve.padelcampus.db.dao.NewsDAOI;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.utils.SessionUtil;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/matchmaker")
public class MatchMakerController extends BaseController{
    
    @Autowired
    NewsDAOI newsDAO;
    
    @Autowired
    MatchOfferDAOI matchOfferDAO;
    
    @Autowired
    SessionUtil sessionUtil;
    
    @RequestMapping()
    public ModelAndView getIndex(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("matchmaker/index");
        mav.addObject("Models", matchOfferDAO.findCurrent());
        Player user = sessionUtil.getUser(request);
        if (user!=null){
            mav.addObject("PersonalOffers", matchOfferDAO.findBy(user));
        }
        return mav;
    }
    
    @RequestMapping("profile")
    public ModelAndView getProfile(HttpServletRequest request){
        sessionUtil.setProfileRedirectPath(request, "/matchmaker");
        return new ModelAndView("redirect:/account/profile");
    }
}
