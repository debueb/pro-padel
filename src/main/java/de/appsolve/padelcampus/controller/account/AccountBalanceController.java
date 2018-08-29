/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.account;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.dao.TransactionDAOI;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/account/balance")
public class AccountBalanceController extends BaseController {

    @Autowired
    PlayerDAOI playerDAO;

    @Autowired
    TransactionDAOI transactionDAO;

    @Autowired
    SessionUtil sessionUtil;

    @RequestMapping()
    public ModelAndView getIndex(HttpServletRequest request) {
        Player player = sessionUtil.getUser(request);
        //refresh player metadata
        player = playerDAO.findByUUID(player.getUUID());
        ModelAndView mav = new ModelAndView("account/balance/index");
        mav.addObject("Player", player);
        mav.addObject("Transactions", transactionDAO.findByPlayer(player));
        return mav;
    }


}
