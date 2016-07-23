package de.appsolve.padelcampus.db.dao;
;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAO;
import java.util.List;
import de.appsolve.padelcampus.db.model.Booking;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class BookingBaseDAO extends BaseEntityDAO<Booking> implements BookingBaseDAOI{

   @Override
   @SuppressWarnings("unchecked")
    public List<Booking> findBlockedBookings() {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.isNotNull("blockingTime"));
        criteria.add(Restrictions.or(Restrictions.isNull("cancelled"), Restrictions.eq("cancelled", false)));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<Booking>) criteria.list();
    }

    @Override
    public void cancelBooking(Booking booking) {
        booking.setBlockingTime(null);
        booking.setCancelled(true);
        booking.setCancelReason("Session Timeout");
        saveOrUpdate(booking);
    }
}