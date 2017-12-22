package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.SortedBaseDAO;
import de.appsolve.padelcampus.db.model.Community;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dominik
 */
@Component
public class CommunityDAO extends SortedBaseDAO<Community> implements CommunityDAOI {

    @Override
    protected Set<String> getIndexedProperties() {
        return new HashSet<>(Arrays.asList("name"));
    }

    @Override
    public Community findByName(String name) {
        return super.findByAttribute("name", name);
    }
}
