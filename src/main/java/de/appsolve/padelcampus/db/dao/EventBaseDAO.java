package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAO;
import de.appsolve.padelcampus.db.model.Event;
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
public class EventBaseDAO extends BaseEntityDAO<Event> implements EventBaseDAOI {

  @Override
  @SuppressWarnings("unchecked")
  public List<Event> findActiveEventsExpiredBefore(LocalDate date) {
    Criteria criteria = getCriteria();
    criteria.add(Restrictions.lt("endDate", date));
    criteria.add(Restrictions.eq("active", Boolean.TRUE));
    criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
    return (List<Event>) criteria.list();
  }

}
