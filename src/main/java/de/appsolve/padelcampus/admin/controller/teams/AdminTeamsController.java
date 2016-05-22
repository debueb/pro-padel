/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.teams;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/teams")
public class AdminTeamsController extends AdminBaseController<Team> {
    
    @Autowired
    PlayerDAOI playerDAO;
    
    @Autowired
    TeamDAOI teamDAO;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Set.class, "players", new CustomCollectionEditor(Set.class) {
            @Override
            protected Object convertElement(Object element) {
                Long id = Long.parseLong((String) element);
                return playerDAO.findById(id);
            }
        });
    }
    
    @Override
    public ModelAndView getEditView(Team team) {
        ModelAndView mav = new ModelAndView("admin/teams/edit", "Model", team);
        Set<Player> teamPlayers = team.getPlayers();
        List<Player> players = playerDAO.findAll();
        players.removeAll(teamPlayers);
        mav.addObject("TeamPlayers", teamPlayers);
        mav.addObject("AllPlayers", players);
        return mav;
    }
    
    @Override
    public ModelAndView postEditView(@ModelAttribute("Model") Team model, HttpServletRequest request, BindingResult result){
        //derive team name from players if it is empty
        if (StringUtils.isEmpty(model.getName())){
            for (Player player: model.getPlayers()){
                String name = model.getName();
                if (StringUtils.isEmpty(name)){
                    name = "";
                } else {
                    name += " / ";
                }
                model.setName(name + player.getLastName());
            }
        }
        return super.postEditView(model, request, result);
    }

    @Override
    public BaseEntityDAOI getDAO() {
        return teamDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/teams";
    }
    
    @Override
    protected List<Team> findAll() {
        return teamDAO.findAllFetchWithPlayers();
    }

    @Override
    protected Team findById(Long modelId) {
        return teamDAO.findByIdFetchWithPlayers(modelId);
    }
}
