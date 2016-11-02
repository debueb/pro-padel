package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.SortedGenericDAO;
import de.appsolve.padelcampus.db.model.OfferOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class OfferOptionDAO extends SortedGenericDAO<OfferOption> implements OfferOptionDAOI{
    
    @Override
    protected Set<String> getIndexedProperties(){
       return new HashSet<>(Arrays.asList("name")); 
    }
}
