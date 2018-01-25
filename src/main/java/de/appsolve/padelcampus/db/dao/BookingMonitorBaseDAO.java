package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAO;
import de.appsolve.padelcampus.db.model.BookingMonitor;
import org.hibernate.Criteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.stereotype.Component;

import java.util.List;

;

/**
 * @author dominik
 */
@Component
public class BookingMonitorBaseDAO extends BaseEntityDAO<BookingMonitor> implements BookingMonitorBaseDAOI {

    @Override
    @SuppressWarnings("unchecked")
    public List<BookingMonitor> findOldBookingMonitors() {
        Criteria criteria = getCriteria();
        SimpleExpression olderThanToday = Restrictions.lt("bookingDate", LocalDate.now());
        LogicalExpression todayAndOlderThanNow = Restrictions.and(Restrictions.eq("bookingDate", LocalDate.now()), Restrictions.lt("bookingTime", LocalTime.now()));
        criteria.add(Restrictions.or(olderThanToday, todayAndOlderThanNow));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return (List<BookingMonitor>) criteria.list();
    }
}