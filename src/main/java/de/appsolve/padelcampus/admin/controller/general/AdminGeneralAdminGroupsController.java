/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.general;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.constants.Privilege;
import de.appsolve.padelcampus.db.dao.AdminGroupDAOI;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.AdminGroup;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.spring.PlayerCollectionEditor;
import de.appsolve.padelcampus.spring.PrivilegeCollectionPropertyEditor;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import jersey.repackaged.com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
@RequestMapping("/admin/general/admingroups")
public class AdminGeneralAdminGroupsController extends AdminBaseController<AdminGroup> {
    
    @Autowired
    PlayerDAOI playerDAO;
    
    @Autowired
    AdminGroupDAOI adminGroupDAO;
    
    @Autowired
    PlayerCollectionEditor playerCollectionEditor;
    
    @Autowired
    PrivilegeCollectionPropertyEditor privilegeCollectionEditor;
    
    @Autowired
    SessionUtil sessionUtil;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Set.class, "players", playerCollectionEditor);
        binder.registerCustomEditor(Set.class, "privileges", privilegeCollectionEditor);
    }
    
    @Override
    public ModelAndView getEditView(AdminGroup adminGroup) {
        ModelAndView mav = new ModelAndView("admin/general/admingroups/edit", "Model", adminGroup);
        mav.addObject("AdminPlayers", adminGroup.getPlayers());
        List<Player> allPlayers = playerDAO.findAll();
        allPlayers.removeAll(adminGroup.getPlayers());
        mav.addObject("AllPlayers", allPlayers);
        mav.addObject("Privileges", adminGroup.getPrivileges());
        List<Privilege> allPrivileges = new LinkedList<>(Arrays.asList(Privilege.values()));
        allPrivileges.removeAll(adminGroup.getPrivileges());
        mav.addObject("AllPrivileges", allPrivileges);
        return mav;
    }
    
    @Override
    public ModelAndView postEditView(@ModelAttribute("Model") AdminGroup model, HttpServletRequest request, BindingResult result){
        validator.validate(model, result);
        
        Set<Privilege> userPrivileges = sessionUtil.getPrivileges(request);
        Set<Privilege> allPrivileges = new HashSet<>(Arrays.asList(Privilege.values()));
        Sets.SetView<Privilege> forbiddenPrivileges = Sets.difference(allPrivileges, userPrivileges);
        if (!forbiddenPrivileges.isEmpty()){
            Set<Privilege> unauthorizedPrivileges = new HashSet<>(model.getPrivileges());
            unauthorizedPrivileges.retainAll(forbiddenPrivileges);
            if (!unauthorizedPrivileges.isEmpty()){
                result.addError(new ObjectError("*", msg.get("UnauthorizedPrivilegeMsg", new Object[]{unauthorizedPrivileges})));
            }
        }
        
        if (result.hasErrors()){
            return getEditView(model);
        }
        adminGroupDAO.saveOrUpdate(model);
        return redirectToIndex(request);
    }

    @Override
    public BaseEntityDAOI getDAO() {
        return adminGroupDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/general/admingroups";
    }
}
