/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.customers;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.db.dao.CustomerDAOI;
import de.appsolve.padelcampus.db.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/customers")
public class AdminCustomersController extends AdminBaseController<Customer> {
    
    @Autowired
    CustomerDAOI customerDAO;
    
    @Override
    public CustomerDAOI getDAO() {
        return customerDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/customers";
    }
}
