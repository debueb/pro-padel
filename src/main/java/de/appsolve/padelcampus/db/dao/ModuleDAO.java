package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.SortedGenericDAO;
import de.appsolve.padelcampus.db.model.Module;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class ModuleDAO extends SortedGenericDAO<Module> implements ModuleDAOI{

    @Override
    public List<Module> findFooterModules() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("showInFooter", true);
        return findByAttributes(attributes);
    }

    @Override
    public List<Module> findMenuModules() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("showInMenu", true);
        return findByAttributes(attributes);
    }
    
    @Override
    public Module findByTitle(String title) {
        title = title.replace("-", " ");
        return findByAttribute("title", title);
    }
}
