package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.Player;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

;

/**
 * @author dominik
 */
@Component
public class BookingDAO extends GenericDAO<Booking> implements BookingDAOI {

    @Override
    public Booking findByUUID(String UUID) {
        return findByAttribute("UUID", UUID);
    }

    @Override
    public Booking findByUUIDWithEvent(String UUID) {
        return findByUUIDFetchEagerly(UUID, "event");
    }

    @Override
    public Booking findByUUIDWithEventAndPlayers(String UUID) {
        return findByUUIDFetchEagerly(UUID, "event", "players");
    }

    @Override
    public Booking findByIdWithOfferOptions(Long id) {
        return findByIdFetchEagerly(id, "offerOptions");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Booking> findBlockedBookingsForDate(LocalDate date) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("bookingDate", date));
        criteria.add(Restrictions.isNotNull("blockingTime"));
        criteria.add(Restrictions.isNotNull("offer"));
        criteria.add(Restrictions.or(Restrictions.isNull("cancelled"), Restrictions.eq("cancelled", false)));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<Booking>) criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Booking> findBlockedBookingsBetween(LocalDate startDate, LocalDate endDate) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.ge("bookingDate", startDate));
        criteria.add(Restrictions.le("bookingDate", endDate));
        criteria.add(Restrictions.isNotNull("blockingTime"));
        criteria.add(Restrictions.isNotNull("offer"));
        criteria.add(Restrictions.or(Restrictions.isNull("cancelled"), Restrictions.eq("cancelled", false)));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<Booking>) criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Booking> findBlockedBookings() {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.isNotNull("blockingTime"));
        criteria.add(Restrictions.isNotNull("offer"));
        criteria.add(Restrictions.or(Restrictions.isNull("cancelled"), Restrictions.eq("cancelled", false)));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<Booking>) criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Booking> findActiveBookingsBetween(LocalDate startDate, LocalDate endDate) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.ge("bookingDate", startDate));
        criteria.add(Restrictions.le("bookingDate", endDate));
        criteria.add(Restrictions.isNotNull("offer"));
        criteria.add(Restrictions.or(Restrictions.eq("paymentMethod", PaymentMethod.Subscription), Restrictions.eq("paymentMethod", PaymentMethod.Cash), Restrictions.eq("paymentMethod", PaymentMethod.ExternalVoucher), Restrictions.eq("paymentMethod", PaymentMethod.Reservation), Restrictions.eq("paymentMethod", PaymentMethod.Voucher), Restrictions.eq("paymentConfirmed", true)));
        criteria.add(Restrictions.or(Restrictions.isNull("cancelled"), Restrictions.eq("cancelled", false)));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        criteria.addOrder(Order.asc("bookingDate"));
        criteria.addOrder(Order.asc("bookingTime"));
        criteria.addOrder(Order.asc("offer"));
        return (List<Booking>) criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Booking> findActiveReservationsBetween(LocalDate startDate, LocalDate endDate) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.ge("bookingDate", startDate));
        criteria.add(Restrictions.le("bookingDate", endDate));
        criteria.add(Restrictions.isNotNull("offer"));
        criteria.add(Restrictions.or(Restrictions.eq("paymentMethod", PaymentMethod.Reservation)));
        criteria.add(Restrictions.or(Restrictions.isNull("cancelled"), Restrictions.eq("cancelled", false)));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<Booking>) criteria.list();
    }

    @Override
    public List<Booking> findOfferBookingsByPlayer(Player player) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("player", player));
        criteria.add(Restrictions.isNotNull("offer"));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List<Booking> list = criteria.list();
        sort(list);
        return list;
    }

    @Override
    public List<Booking> findAllBookingsByPlayer(Player player) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("player", player));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List<Booking> list = criteria.list();
        sort(list);
        return list;
    }

    @Override
    public List<Booking> findActiveBookingsByPlayerBetween(Player player, LocalDate startDate, LocalDate endDate) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("player", player));
        criteria.add(Restrictions.isNotNull("offer"));
        criteria.add(Restrictions.ge("bookingDate", startDate));
        criteria.add(Restrictions.le("bookingDate", endDate));
        criteria.add(Restrictions.or(Restrictions.isNull("cancelled"), Restrictions.eq("cancelled", false)));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
        List<Booking> list = criteria.list();
        sort(list);
        return list;
    }

    @Override
    public List<Booking> findByBlockingTimeAndComment(LocalDateTime blockingTime, String comment) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("blockingTime", blockingTime);
        attributes.put("comment", comment);
        return findByAttributes(attributes);
    }
}