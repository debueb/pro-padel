package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.SortedGenericDAO;
import de.appsolve.padelcampus.db.model.Offer;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class OfferDAO extends SortedGenericDAO<Offer> implements OfferDAOI{
}
