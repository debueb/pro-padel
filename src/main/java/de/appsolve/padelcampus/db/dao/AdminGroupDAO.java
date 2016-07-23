package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.AdminGroup;
import de.appsolve.padelcampus.db.model.Player;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class AdminGroupDAO extends GenericDAO<AdminGroup> implements AdminGroupDAOI{

    @Override
    @SuppressWarnings("unchecked")
    public List<AdminGroup> findByPlayer(Player player) {
        Criteria criteria = getCriteria();
        criteria.createAlias("players", "p");
        criteria.add(Restrictions.eq("p.id", player.getId()));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return (List<AdminGroup>) criteria.list();
    }
}
