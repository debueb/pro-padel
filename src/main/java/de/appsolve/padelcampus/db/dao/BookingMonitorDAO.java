package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.BookingMonitor;
import de.appsolve.padelcampus.db.model.Player;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

;

/**
 * @author dominik
 */
@Component
public class BookingMonitorDAO extends GenericDAO<BookingMonitor> implements BookingMonitorDAOI {

    @Override
    public List<BookingMonitor> findByPlayerAndDateAndTime(Player player, LocalDate date, LocalTime time) {
        Map<String, Object> map = new HashMap<>();
        map.put("player", player);
        map.put("bookingDate", date);
        map.put("bookingTime", time);
        return super.findByAttributes(map);
    }

    @Override
    public List<BookingMonitor> findByDateAndTime(LocalDate date, LocalTime time) {
        Map<String, Object> map = new HashMap<>();
        map.put("bookingDate", date);
        map.put("bookingTime", time);
        return super.findByAttributes(map);
    }
}