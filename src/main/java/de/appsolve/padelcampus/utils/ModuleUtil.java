/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
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
    ModuleDAOI moduleDAO;
    
    public void reloadModules(ServletContext context) {
        context.setAttribute(Constants.APPLICATION_MENU_LINKS, moduleDAO.findMenuModules());
        context.setAttribute(Constants.APPLICATION_FOOTER_LINKS, moduleDAO.findFooterModules());
    }
    
}
