package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAO;
import de.appsolve.padelcampus.db.model.Customer;
import de.appsolve.padelcampus.db.model.Ranking;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author dominik
 */
@Component
public class RankingBaseDAO extends BaseEntityDAO<Ranking> implements RankingBaseDAOI {

    @Override
    @SuppressWarnings("unchecked")
    public List<Ranking> findByGenderAndDate(Gender gender, LocalDate date, Customer customer) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("customer", customer));
        criteria.add(Restrictions.eq("gender", gender));
        criteria.add(Restrictions.eq("date", date));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List<Ranking> list = criteria.list();
        Collections.sort(list);
        return list;
    }
}
