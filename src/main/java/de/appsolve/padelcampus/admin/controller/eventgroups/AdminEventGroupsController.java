/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.eventgroups;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.db.dao.EventGroupDAOI;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.EventGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/eventgroups")
public class AdminEventGroupsController extends AdminBaseController<EventGroup> {

    @Autowired
    EventGroupDAOI eventGroupDAO;

    @Override
    public BaseEntityDAOI getDAO() {
        return eventGroupDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/eventgroups";
    }


}