package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.Contact;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class ContactDAO extends GenericDAO<Contact> implements ContactDAOI{

    @Override
    public List<Contact> findAllForContactForm() {
        Map<String, Object> map = new HashMap<>();
        map.put("notifyOnContactForm", Boolean.TRUE);
        return findByAttributes(map);
    }

    @Override
    public List<Contact> findAllForBookings() {
        Map<String, Object> map = new HashMap<>();
        map.put("notifyOnBooking", Boolean.TRUE);
        return findByAttributes(map);
    }

    @Override
    public List<Contact> findAllForBookingCancellations() {
        Map<String, Object> map = new HashMap<>();
        map.put("notifyOnBookingCancellation", Boolean.TRUE);
        return findByAttributes(map);
    }

}
