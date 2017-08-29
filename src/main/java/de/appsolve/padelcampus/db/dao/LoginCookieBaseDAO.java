package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAO;
import de.appsolve.padelcampus.db.model.LoginCookie;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dominik
 */
@Component
public class LoginCookieBaseDAO extends BaseEntityDAO<LoginCookie> implements LoginCookieBaseDAOI {

    @Override
    @SuppressWarnings("unchecked")
    public List<LoginCookie> findExpiredBefore(LocalDate date) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.lt("validUntil", date));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<LoginCookie>) criteria.list();
    }
}
