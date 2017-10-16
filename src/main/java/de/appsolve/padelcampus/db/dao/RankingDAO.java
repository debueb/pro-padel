package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.comparators.RankingByDateComparator;
import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.Customer;
import de.appsolve.padelcampus.db.model.Participant;
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
public class RankingDAO extends GenericDAO<Ranking> implements RankingDAOI {

    @Override
    @SuppressWarnings("unchecked")
    public List<Ranking> findByGenderAndDate(Gender gender, LocalDate date) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("gender", gender));
        criteria.add(Restrictions.eq("date", date));
        Customer customer = getCustomer();
        if (customer == null) { // ranking across all customers
            criteria.add(Restrictions.isNull("customer"));
        } else { // customer specific ranking
            criteria.add(Restrictions.eq("customer", customer));
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List<Ranking> list = criteria.list();
        Collections.sort(list);
        return list;
    }

    public List<Ranking> findByParticipantBetweenDates(Participant participant, Gender gender, LocalDate startDate, LocalDate endDate) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("gender", gender));
        criteria.add(Restrictions.ge("date", startDate));
        criteria.add(Restrictions.lt("date", endDate));
        criteria.add(Restrictions.eq("participant", participant));
        Customer customer = getCustomer();
        if (customer == null) { // ranking across all customers
            criteria.add(Restrictions.isNull("customer"));
        } else { // customer specific ranking
            criteria.add(Restrictions.eq("customer", customer));
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List<Ranking> list = criteria.list();
        Collections.sort(list, new RankingByDateComparator());
        return list;
    }
}
