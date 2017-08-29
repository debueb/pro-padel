package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.SortedGenericDAO;
import de.appsolve.padelcampus.db.model.Offer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author dominik
 */
@Component
public class OfferDAO extends SortedGenericDAO<Offer> implements OfferDAOI {

    @Override
    protected Set<String> getIndexedProperties() {
        return new HashSet<>(Arrays.asList("name"));
    }

    @Override
    public Offer findByIdFetchWithOfferOptions(Long id) {
        return findByIdFetchEagerly(id, "offerOptions");
    }

    @Override
    public List<Offer> findAllFetchWithOfferOptions() {
        return findAllFetchEagerly("offerOptions");
    }
}
