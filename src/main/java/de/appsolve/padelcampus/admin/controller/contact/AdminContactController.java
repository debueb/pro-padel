/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.contact;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.db.dao.ContactDAOI;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/contact")
public class AdminContactController extends AdminBaseController<Contact> {

    @Autowired
    ContactDAOI contactDAO;

    @Override
    public BaseEntityDAOI getDAO() {
        return contactDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/contact";
    }
}
