package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.model.Customer;
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
    public List<Module> findAllFooterModules() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("showInFooter", true);
        return findByAttributesForAllCustomers(attributes);
    }

    @Override
    public List<Module> findAllMenuModules() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("showInMenu", true);
        return findByAttributesForAllCustomers(attributes);
    }
    
    @Override
    public List<Module> findFooterModules(Customer customer) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("showInFooter", true);
        attributes.put("customer", customer);
        return findByAttributesForAllCustomers(attributes);
    }

    @Override
    public List<Module> findMenuModules(Customer customer) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("showInMenu", true);
        attributes.put("customer", customer);
        return findByAttributesForAllCustomers(attributes);
    }
    
    @Override
    public Module findByTitle(String title) {
        return findByAttribute("title", title);
    }
}
