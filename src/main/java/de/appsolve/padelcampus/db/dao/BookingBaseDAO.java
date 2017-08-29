package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAO;
import de.appsolve.padelcampus.db.model.Booking;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.stereotype.Component;

import java.util.List;

;

/**
 * @author dominik
 */
@Component
public class BookingBaseDAO extends BaseEntityDAO<Booking> implements BookingBaseDAOI {

    @Override
    @SuppressWarnings("unchecked")
    public List<Booking> findUnpaidBlockingBookings() {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.isNotNull("blockingTime"));
        criteria.add(Restrictions.or(Restrictions.isNull("cancelled"), Restrictions.eq("cancelled", false)));
        criteria.add(Restrictions.or(Restrictions.isNull("confirmed"), Restrictions.eq("confirmed", false)));
        criteria.add(Restrictions.ne("paymentMethod", PaymentMethod.Reservation));
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

    @Override
    @SuppressWarnings("unchecked")
    public List<Booking> findCurrentBookingsWithOfferOptions(LocalDate date, LocalTime time) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("bookingDate", date));
        criteria.add(Restrictions.eq("bookingTime", time));
        criteria.setFetchMode("offerOptions", FetchMode.JOIN);
        criteria.add(Restrictions.isNotNull("offerOptions"));
        criteria.add(Restrictions.or(Restrictions.isNull("cancelled"), Restrictions.eq("cancelled", false)));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<Booking>) criteria.list();
    }
}