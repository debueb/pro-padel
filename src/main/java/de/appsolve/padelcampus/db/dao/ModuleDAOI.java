/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.constants.ModuleType;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.Module;
import java.util.List;

/**
 *
 * @author dominik
 */
public interface ModuleDAOI extends BaseEntityDAOI<Module>{
    
    public Module findByPosition(Long position);
    public Module findByUrlTitle(String title);
    public List<Module> findByModuleType(ModuleType moduleType);
    public List<Module> findAllRootModules();
    public Module findParent(Module module);
}
