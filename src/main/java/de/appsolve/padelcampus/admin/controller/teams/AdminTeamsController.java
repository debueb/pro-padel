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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    
    @Override
    public ModelAndView showIndex(HttpServletRequest request, @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(required = false, name = "search") String search){
        return super.showIndex(request, pageable, search);
    }
    
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
    public BaseEntityDAOI getDAO() {
        return teamDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/teams";
    }
    
    @Override
    protected Page<Team> findAll(Pageable pageable) {
        Page<Team> findAllFetchWithPlayers = teamDAO.findAllFetchWithPlayers(pageable);
        return findAllFetchWithPlayers;
    }
    
    @Override
    protected Page<Team> findAllByFuzzySearch(String search) {
        return teamDAO.findAllByFuzzySearch(search, "players");
    }

    @Override
    protected Team findById(Long modelId) {
        return teamDAO.findByIdFetchWithPlayers(modelId);
    }
}
