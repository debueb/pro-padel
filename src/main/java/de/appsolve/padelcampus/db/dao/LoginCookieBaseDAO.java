package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAO;
import de.appsolve.padelcampus.db.model.LoginCookie;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class LoginCookieBaseDAO extends BaseEntityDAO<LoginCookie> implements LoginCookieBaseDAOI{

    @Override
    public List<LoginCookie> findExpiredBefore(LocalDate date) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.lt("validUntil", date));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<LoginCookie>) criteria.list();
    }
}
