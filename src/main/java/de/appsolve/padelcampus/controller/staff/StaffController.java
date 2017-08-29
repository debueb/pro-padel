/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.staff;

import de.appsolve.padelcampus.constants.ModuleType;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.dao.StaffMemberDAOI;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.db.model.StaffMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/staff")
public class StaffController extends BaseController {

    @Autowired
    StaffMemberDAOI staffMemberDAO;

    @Autowired
    ModuleDAOI moduleDAO;

    @RequestMapping
    public ModelAndView getIndex() {
        List<Module> modules = moduleDAO.findByModuleType(ModuleType.Staff);
        Module module = null;
        if (!modules.isEmpty()) {
            module = modules.get(0);
        }
        List<StaffMember> staffMembers = staffMemberDAO.findAll();
        return getIndexView(staffMembers, module);
    }

    private ModelAndView getIndexView(List<StaffMember> staffMembers, Module module) {
        ModelAndView mav = new ModelAndView("staff/index");
        mav.addObject("Module", module);
        mav.addObject("Models", staffMembers);
        return mav;
    }
}
