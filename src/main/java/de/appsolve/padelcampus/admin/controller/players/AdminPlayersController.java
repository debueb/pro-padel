/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.players;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.Player;
import javax.servlet.http.HttpServletRequest;
import org.jadira.usertype.spi.utils.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/players")
public class AdminPlayersController extends AdminBaseController<Player> {
    
    @Autowired
    PlayerDAOI playerDAO;
    
    @Override
    public ModelAndView showIndex(HttpServletRequest request, @PageableDefault(size = 10, sort = "firstName", direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(required = false, name = "search") String search){
        return super.showIndex(request, pageable, search);
    }
    
    @Override
    public ModelAndView postEditView(@ModelAttribute("Model") Player model, HttpServletRequest request, BindingResult result){
        validator.validate(model, result);
        if (result.hasErrors()){
            return getEditView(model);
        }
        //make sure not to overwrite passwordHash, verfied etc.
        Player player;
        if (model.getId() != null){
            player = playerDAO.findById(model.getId());
        } else {
            Player existingPlayer = playerDAO.findByEmail(model.getEmail());
            if (existingPlayer!=null){
                result.addError(new ObjectError("email", msg.get("EmailAlreadyRegistered")));
                return getEditView(model);
            }
            player = new Player();
        }
        player.setEmail(model.getEmail());
        player.setFirstName(model.getFirstName());
        player.setLastName(model.getLastName());
        player.setPhone(model.getPhone());
        player.setGender(model.getGender());
        playerDAO.saveOrUpdate(player);
        return redirectToIndex(request);
    }
    
    @Override
    public BaseEntityDAOI getDAO() {
        return playerDAO;
    }
    
    @Override
    public String getModuleName() {
        return "admin/players";
    }
}
