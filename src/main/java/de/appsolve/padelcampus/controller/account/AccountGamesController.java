/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.account;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.exceptions.ResourceNotFoundException;
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
        if (player == null){
            throw new ResourceNotFoundException();
        }
        ModelAndView mav = new ModelAndView("account/games/index");
        mav.addObject("Player", player);
        mav.addObject("GameResultMap", gameUtil.getGameResultMap(player, sortBy));
        return mav;
    }
}
