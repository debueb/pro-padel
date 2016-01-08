/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.model.Customer;
import de.appsolve.padelcampus.db.model.Module;
import java.util.List;

/**
 *
 * @author dominik
 */
public interface ModuleDAOI extends GenericDAOI<Module>{
    
    public List<Module> findAllFooterModules();
    public List<Module> findAllMenuModules();
    public List<Module> findFooterModules(Customer customer);
    public List<Module> findMenuModules(Customer customer);
    public Module findByTitle(String title);
}
