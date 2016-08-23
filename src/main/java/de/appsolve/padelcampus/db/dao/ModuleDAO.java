package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.constants.ModuleType;
import de.appsolve.padelcampus.db.dao.generic.SortedGenericDAO;
import de.appsolve.padelcampus.db.model.Module;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class ModuleDAO extends SortedGenericDAO<Module> implements ModuleDAOI{

    @Override
    public Module saveOrUpdate(Module entity) {
        if (entity.getSubModules()!=null){
            for (Module subModule: entity.getSubModules()){
                subModule.setCustomer(getCustomer());
            }
        }
        setCustomer(entity);
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(entity);
        return entity;
    }
    
    @Override
    public Module findByTitle(String title) {
        title = title.replace("-", " ");
        return findByAttribute("title", title);
    }

    @Override
    public List<Module> findByModuleType(ModuleType moduleType) {
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put("moduleType", moduleType);
        return findByAttributes(attributeMap);
    }

    @Override
    public List<Module> findAllRootModules() {
        //only get top level modules (e.g. remove all submodules)
        List<Module> modules = findAll();
        Set<Module> modulesToRemove = new HashSet<>();
        for (Module module: modules){
            if (module.getSubModules() != null){
                modulesToRemove.addAll(module.getSubModules());
            }
        }
        modules.removeAll(modulesToRemove);
        return modules;
    }
}
