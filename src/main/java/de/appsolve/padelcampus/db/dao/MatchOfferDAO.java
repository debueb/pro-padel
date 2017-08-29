package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.MatchOffer;
import de.appsolve.padelcampus.db.model.Player;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dominik
 */
@Component
public class MatchOfferDAO extends GenericDAO<MatchOffer> implements MatchOfferDAOI {

    @Override
    @SuppressWarnings("unchecked")
    public List<MatchOffer> findCurrent() {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.ge("startDate", new LocalDate()));
        criteria.addOrder(Order.asc("startDate"));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<MatchOffer>) criteria.list();
    }

    @Override
    public List<MatchOffer> findBy(Player player) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("owner", player);
        return findByAttributes(attributes);
    }
}
