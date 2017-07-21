/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.account;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.utils.GameUtil;
import de.appsolve.padelcampus.utils.SessionUtil;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/account/games")
public class AccountGamesController extends BaseController {
    
    @Autowired
    SessionUtil sessionUtil;
    
    @Autowired
    GameUtil gameUtil;
    
    @RequestMapping()
    public ModelAndView getIndex(HttpServletRequest request, @RequestParam(defaultValue = "date") String sortBy){
        Player player = sessionUtil.getUser(request);
        ModelAndView mav = new ModelAndView("account/games/index");
        return gameUtil.getGameView(mav, player, sortBy);
    }
}
