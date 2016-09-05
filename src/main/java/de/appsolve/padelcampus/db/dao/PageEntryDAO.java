package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.SortedGenericDAO;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.db.model.PageEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class PageEntryDAO extends SortedGenericDAO<PageEntry> implements PageEntryDAOI{

    @Override
    public List<PageEntry> findByModule(Module module) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("module", module);
        return findByAttributes(attributes);
    }
}
