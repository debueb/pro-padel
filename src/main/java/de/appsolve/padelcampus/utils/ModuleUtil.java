/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.data.CustomerI;
import de.appsolve.padelcampus.data.DefaultCustomer;
import de.appsolve.padelcampus.db.dao.CustomerDAOI;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.model.Customer;
import de.appsolve.padelcampus.db.model.Module;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class ModuleUtil {
    
    @Autowired
    CustomerDAOI customerDAO;
    
    @Autowired
    ModuleDAOI moduleDAO;
    
    public void reloadModules(ServletContext context) {
        Map<String, List<Module>> customerMenuModules = new HashMap<>();
        Map<String, List<Module>> customerFooterModules = new HashMap<>();
        List<Customer> allCustomers = customerDAO.findAllforAllCustomers();
        if (allCustomers.isEmpty()){
            CustomerI defaultCustomer = new DefaultCustomer();
            customerMenuModules.put(defaultCustomer.getName(), moduleDAO.findAllMenuModules());
            customerFooterModules.put(defaultCustomer.getName(), moduleDAO.findAllFooterModules());
        } else {
            for (Customer customer: allCustomers){
                customerMenuModules.put(customer.getName(), moduleDAO.findMenuModules(customer));
                customerFooterModules.put(customer.getName(), moduleDAO.findFooterModules(customer));
            }
        }
        context.setAttribute(Constants.APPLICATION_MENU_LINKS, customerMenuModules);
        context.setAttribute(Constants.APPLICATION_FOOTER_LINKS, customerFooterModules);
    }    
}
