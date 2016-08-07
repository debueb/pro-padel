/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.teams;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.db.dao.CommunityDAOI;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.Community;
import de.appsolve.padelcampus.db.model.Team;
import de.appsolve.padelcampus.spring.CommunityPropertyEditor;
import de.appsolve.padelcampus.spring.PlayerCollectionEditor;
import de.appsolve.padelcampus.utils.TeamUtil;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    
    @Autowired
    CommunityDAOI communityDAO;
    
    @Autowired
    PlayerCollectionEditor playerCollectionEditor;
    
    @Autowired
    CommunityPropertyEditor communityPropertyEditor;
    
    @Override
    public ModelAndView showIndex(HttpServletRequest request, @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(required = false, name = "search") String search){
        return super.showIndex(request, pageable, search);
    }
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Set.class, "players", playerCollectionEditor);
        binder.registerCustomEditor(Community.class, communityPropertyEditor);
    }
    
    @Override
    protected ModelAndView getEditView(Team model){
        ModelAndView mav =  super.getEditView(model);
        mav.addObject("Communities", communityDAO.findAll());
        return mav;
    }
    
    @Override
    public ModelAndView postEditView(@ModelAttribute("Model") Team model, HttpServletRequest request, BindingResult result){
        //derive team name from players if it is empty
        if (StringUtils.isEmpty(model.getName())){
            model.setName(TeamUtil.getTeamName(model));
        }
        model.setName(model.getName());
        
        validator.validate(model, result);
        if (result.hasErrors()){
            return getEditView(model);
        }
        
        //make sure team does not already exist
        if (model.getId()==null){
            Team existingTeam = teamDAO.findByPlayers(model.getPlayers());
            if (existingTeam != null){
                result.addError(new ObjectError("*", msg.get("TeamAlreadyExistsWithName", new Object[]{existingTeam})));
                return getEditView(model);
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
    protected Page<Team> findAll(Pageable pageable) {
        Page<Team> findAllFetchWithPlayers = teamDAO.findAllFetchWithPlayers(pageable);
        return findAllFetchWithPlayers;
    }
    
    @Override
    protected Team findById(Long modelId) {
        return teamDAO.findByIdFetchWithPlayers(modelId);
    }
}
