package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Transaction;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dominik
 */
@Component
public class TransactionDAO extends GenericDAO<Transaction> implements TransactionDAOI {

    @Override
    public List<Transaction> findByPlayer(Player player) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("player", player));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        criteria.addOrder(Order.asc("date"));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }
}
