package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.constants.ModuleType;
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
}
