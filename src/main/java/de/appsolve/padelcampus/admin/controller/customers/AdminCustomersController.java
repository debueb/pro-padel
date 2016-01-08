/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.customers;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.db.dao.CustomerDAOI;
import de.appsolve.padelcampus.db.dao.GenericDAOI;
import de.appsolve.padelcampus.db.model.Customer;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView showIndex(HttpServletRequest request){
        List<Customer> all = customerDAO.findAllforAllCustomers();
        Collections.sort(all);
        return getIndexView(all);
    }
    
    @Override
    public GenericDAOI getDAO() {
        return customerDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/customers";
    }
}
