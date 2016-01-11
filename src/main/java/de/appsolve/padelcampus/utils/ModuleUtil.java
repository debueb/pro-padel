/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.data.CustomerI;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.model.Module;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class ModuleUtil {
    
    @Autowired
    ModuleDAOI moduleDAO;
    
    @Autowired
    SessionUtil sessionUtil;
    
    public void initModules(HttpServletRequest request) {
        CustomerI customer = sessionUtil.getCustomer(request);
        
        ServletContext context = request.getServletContext();
        Map<String, List<Module>> links = (Map<String, List<Module>>) context.getAttribute(Constants.APPLICATION_MENU_LINKS);
        Map<String, List<Module>> footerLinks = (Map<String, List<Module>>) context.getAttribute(Constants.APPLICATION_MENU_LINKS);
        if (links == null || footerLinks == null){
            links = new HashMap<>();
            footerLinks = new HashMap<>();
        }
        String customerName = customer.getName();
        if (!links.containsKey(customerName) || !footerLinks.containsKey(customerName)){
            List<Module> modules = moduleDAO.findMenuModules();
            List<Module> footerModules = moduleDAO.findFooterModules();
            links.put(customer.getName(), modules);
            footerLinks.put(customer.getName(), footerModules);
            context.setAttribute(Constants.APPLICATION_MENU_LINKS, links);
            context.setAttribute(Constants.APPLICATION_FOOTER_LINKS, footerLinks);
        }
    } 
    
    public void reloadModules(HttpServletRequest request) {
        ServletContext context = request.getServletContext();
        context.setAttribute(Constants.APPLICATION_MENU_LINKS, null);
        context.setAttribute(Constants.APPLICATION_FOOTER_LINKS, null);
        initModules(request);
    }
}
