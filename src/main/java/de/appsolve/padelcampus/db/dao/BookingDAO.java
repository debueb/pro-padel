package de.appsolve.padelcampus.db.dao;
;
import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.constants.PaymentMethod;
import java.util.List;
import org.joda.time.LocalDate;
import de.appsolve.padelcampus.db.model.Booking;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class BookingDAO extends GenericDAO<Booking> implements BookingDAOI{

    @Override
    public Booking findByUUID(String UUID) {
        return findByAttribute("UUID", UUID);
    }    

    @Override
    public List<Booking> findBlockedBookingsForDate(LocalDate date) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("bookingDate", date));
        criteria.add(Restrictions.isNotNull("blockingTime"));
        criteria.add(Restrictions.or(Restrictions.isNull("cancelled"), Restrictions.eq("cancelled", false)));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<Booking>) criteria.list();
    }
    
    @Override
    public List<Booking> findBlockedBookingsBetween(LocalDate startDate, LocalDate endDate) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.ge("bookingDate", startDate));
        criteria.add(Restrictions.le("bookingDate", endDate));
        criteria.add(Restrictions.isNotNull("blockingTime"));
        criteria.add(Restrictions.or(Restrictions.isNull("cancelled"), Restrictions.eq("cancelled", false)));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<Booking>) criteria.list();
    }

    @Override
    public List<Booking> findBlockedBookings() {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.isNotNull("blockingTime"));
        criteria.add(Restrictions.or(Restrictions.isNull("cancelled"), Restrictions.eq("cancelled", false)));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<Booking>) criteria.list();
    }

    @Override
    public List<Booking> findActiveBookingsBetween(LocalDate startDate, LocalDate endDate) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.ge("bookingDate", startDate));
        criteria.add(Restrictions.le("bookingDate", endDate));
        criteria.add(Restrictions.or(Restrictions.eq("paymentMethod", PaymentMethod.Cash), Restrictions.eq("paymentConfirmed", true), Restrictions.eq("paymentMethod", PaymentMethod.Voucher)));
        criteria.add(Restrictions.or(Restrictions.isNull("cancelled"), Restrictions.eq("cancelled", false)));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<Booking>) criteria.list();
    }

    @Override
    public List<Booking> findReservations() {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("paymentMethod", PaymentMethod.Reservation));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<Booking>) criteria.list();
    }

    @Override
    public List<Booking> findByPlayer(Player player) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("player", player);
        return findByAttributes(attributes);
    }
}