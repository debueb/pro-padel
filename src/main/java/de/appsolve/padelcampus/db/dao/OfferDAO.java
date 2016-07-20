package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.SortedGenericDAO;
import de.appsolve.padelcampus.db.model.Offer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class OfferDAO extends SortedGenericDAO<Offer> implements OfferDAOI{
    
    @Override
    protected Set<String> getIndexedProperties(){
       return new HashSet<>(Arrays.asList("name")); 
    }
}
