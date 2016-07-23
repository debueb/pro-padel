/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.constants.CalendarWeekDay;
import static de.appsolve.padelcampus.constants.Constants.NO_HOLIDAY_KEY;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class CalendarConfigUtil {
    
    public List<CalendarConfig> getCalendarConfigsMatchingDate(List<CalendarConfig> configs, LocalDate date){
        List<CalendarConfig> configsMatchingDate = new ArrayList<>();
        Iterator<CalendarConfig> iterator = configs.iterator();
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
                HolidayManager countryHolidays = HolidayManager.getInstance(ManagerParameters.create(HolidayCalendar.valueOf(country)));
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
        Collections.sort(configsMatchingDate);
        return configsMatchingDate;
    }
    
}
