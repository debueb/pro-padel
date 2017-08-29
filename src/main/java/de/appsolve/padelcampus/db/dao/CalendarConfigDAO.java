package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.exceptions.CalendarConfigException;
import de.appsolve.padelcampus.utils.CalendarConfigUtil;
import de.appsolve.padelcampus.utils.Msg;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

;

/**
 * @author dominik
 */
@Component
public class CalendarConfigDAO extends GenericDAO<CalendarConfig> implements CalendarConfigDAOI {

    @Autowired
    Msg msg;

    @Autowired
    CalendarConfigUtil calendarConfigUtil;

    @Override
    public List<CalendarConfig> findBetween(LocalDate startDate, LocalDate endDate) {
        List<CalendarConfig> allConfigs = super.findAll();
        Iterator<CalendarConfig> iterator = allConfigs.iterator();
        while (iterator.hasNext()) {
            CalendarConfig config = iterator.next();
            if (config.getStartDate().compareTo(endDate) > 0) {
                iterator.remove();
            }
            if (config.getEndDate().compareTo(startDate) < 0) {
                iterator.remove();
            }
        }
        return allConfigs;
    }

    @Override
    public List<CalendarConfig> findFor(LocalDate date) throws CalendarConfigException {
        List<CalendarConfig> allConfigs = super.findAll();
        List<CalendarConfig> configsMatchingDate = calendarConfigUtil.getCalendarConfigsMatchingDate(allConfigs, date);
        if (configsMatchingDate.isEmpty()) {
            throw new CalendarConfigException(msg.get("NoMatchingCalendarConfigurationFound"));
        }
        return configsMatchingDate;
    }
}