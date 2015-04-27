/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.model.Module;
import java.util.List;

/**
 *
 * @author dominik
 */
public interface ModuleDAOI extends GenericDAOI<Module>{
    
    List<Module> findFooterModules();
    List<Module> findMenuModules();
}
