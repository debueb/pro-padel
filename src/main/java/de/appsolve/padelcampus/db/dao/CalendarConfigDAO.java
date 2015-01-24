package de.appsolve.padelcampus.db.dao;
;
import de.appsolve.padelcampus.constants.CalendarWeekDay;
import static de.appsolve.padelcampus.constants.Constants.NO_HOLIDAY_KEY;
import java.util.List;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.exceptions.CalendarConfigException;
import de.appsolve.padelcampus.utils.Msg;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dominik
 */
@Component
public class CalendarConfigDAO extends GenericDAO<CalendarConfig> implements CalendarConfigDAOI{
    
    @Autowired
    Msg msg;

    @Override
    public List<CalendarConfig> findBetween(LocalDate startDate, LocalDate endDate) {
        List<CalendarConfig> allConfigs = super.findAll();
        Iterator<CalendarConfig> iterator = allConfigs.iterator();
        while (iterator.hasNext()){
            CalendarConfig config = iterator.next();
            if (config.getStartDate().compareTo(endDate) > 0){
                iterator.remove();
            }
            if (config.getEndDate().compareTo(startDate) < 0){
                iterator.remove();
            }
        }
        return allConfigs;
    }
    
    @Override
    public List<CalendarConfig> findFor(LocalDate date) throws CalendarConfigException{
        List<CalendarConfig> allConfigs = super.findAll();
        List<CalendarConfig> configsMatchingDate = new ArrayList<>();
        Iterator<CalendarConfig> iterator = allConfigs.iterator();
        while (iterator.hasNext()){
            CalendarConfig config = iterator.next();

            //remove configurations that do not match the requested week day
            boolean matchesWeekDay = false;
            for (CalendarWeekDay weekDay: config.getCalendarWeekDays()){
                if (weekDay.ordinal()+1 == date.getDayOfWeek()){
                    matchesWeekDay = true;
                    break;
                }
            }
            if (!matchesWeekDay){
                continue;
            }
            
            //remove configurations that are defined holidays
            String holidayKey = config.getHolidayKey();
            boolean isHoliday = false;
            if (!holidayKey.equals(NO_HOLIDAY_KEY)){
                String[] holidayKeySplit = holidayKey.split("-");
                String country = holidayKeySplit[0];
                String region  = holidayKeySplit[1];
                HolidayManager countryHolidays = HolidayManager.getInstance(HolidayCalendar.valueOf(country));
                isHoliday = countryHolidays.isHoliday(date, region);
            }
            if (isHoliday){
                continue;
            }
            
            //remove configurations that start after the requested date
            if (config.getStartDate().compareTo(date) > 0){
                continue;
            }
            
            //remove configurations that end before the requested date
            if (config.getEndDate().compareTo(date) < 0){
                continue;
            }
            
            configsMatchingDate.add(config);
        }
        if (configsMatchingDate.isEmpty()){
            throw new CalendarConfigException(msg.get("NoMatchingCalendarConfigurationFound"));
        }
        return configsMatchingDate;
    }
    
    @Override
    //disable auto commit on method exit as we modify the CalendarConfig's endTime in certain situations
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public CalendarConfig findFor(LocalDate date, LocalTime time) throws CalendarConfigException{
        List<CalendarConfig> configsMatchingDate = findFor(date);
        List<CalendarConfig> configsMatchingDateAndTime = new ArrayList<>();
        Iterator<CalendarConfig> iterator = configsMatchingDate.iterator();
        while (iterator.hasNext()){
            CalendarConfig config = iterator.next();
            
            //remove configurations that start after the requested time
            if (config.getStartTime().compareTo(time) > 0){
                continue;
            }
            
            //remove configurations that end before the requested time
            if (config.getEndTime().compareTo(time) <= 0){
                continue;
            }
            configsMatchingDateAndTime.add(config);
        }
        
        if (configsMatchingDateAndTime.isEmpty()){
            throw new CalendarConfigException(msg.get("NoMatchingCalendarConfigurationFound"));
        }
        
        if (configsMatchingDateAndTime.size()>0){
            //sort calendar configurations by priority
            Collections.sort(configsMatchingDateAndTime);
            CalendarConfig matchingConfig = configsMatchingDateAndTime.get(0);
        
            //make sure to end lower prioritized configuration before higher prioritized configs start
            //to avoid long durations on lower prioritized time slots, which would allow bookings on lower prioritized conditions during higher prioritized time slots
            for (CalendarConfig otherConfig: configsMatchingDate){
                if (otherConfig.getPriority().compareTo(matchingConfig.getPriority()) > 0){
                    if (otherConfig.getStartTime().isAfter(time)){
                        if (otherConfig.getStartTime().compareTo(matchingConfig.getEndTime()) <= 0){
                            matchingConfig.setEndTimeHour(otherConfig.getStartTimeHour());
                            matchingConfig.setEndTimeMinute(otherConfig.getStartTimeMinute());
                        }
                    }
                }
            }
            return matchingConfig;
        }
        return null;
    }
}