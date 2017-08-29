package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAO;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Subscription;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author dominik
 */
@Component
public class SubscriptionDAO extends BaseEntityDAO<Subscription> implements SubscriptionDAOI {

    @Override
    @SuppressWarnings("unchecked")
    public List<Subscription> findByPlayer(Player player) {
        if (player == null) {
            return Collections.<Subscription>emptyList();
        }
        Criteria criteria = getCriteria();
        criteria.createAlias("players", "p");
        criteria.add(Restrictions.eq("p.id", player.getId()));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return (List<Subscription>) criteria.list();
    }
}
