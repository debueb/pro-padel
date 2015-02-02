/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.utils.SessionUtil;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/account/profile")
public class AccountProfileController extends BaseController {
    
    @Autowired
    PlayerDAOI playerDAO;
    
    @Autowired
    SessionUtil sessionUtil;
    
    @RequestMapping()
    public ModelAndView getIndex(HttpServletRequest request){
        Player user = sessionUtil.getUser(request);
        return getIndexView(user);
    }
    
    @RequestMapping(method=POST)
    public ModelAndView postIndex(@ModelAttribute("Model") Player player, BindingResult bindingResult, HttpServletRequest request){
        ModelAndView profileView = getIndexView(player);
        if (bindingResult.hasErrors()){
            return profileView;
        }
        //make sure nobody changes another player's account
        Player sessionUser = sessionUtil.getUser(request);
        if (sessionUser.getId()!=null && sessionUser.getId().equals(player.getId())){
            //make sure not to overwrite any existing data
            Player persistedPlayer = playerDAO.findById(player.getId());
            persistedPlayer.setFirstName(player.getFirstName());
            persistedPlayer.setLastName(player.getLastName());
            persistedPlayer.setEmail(player.getEmail());
            persistedPlayer.setPhone(player.getPhone());
            playerDAO.saveOrUpdate(persistedPlayer);
            sessionUtil.setUser(request, persistedPlayer);
        }
        return new ModelAndView("redirect:/");
    }

    private ModelAndView getIndexView(Player user) {
        return new ModelAndView("account/profile/index", "Model", user);
    }
}
