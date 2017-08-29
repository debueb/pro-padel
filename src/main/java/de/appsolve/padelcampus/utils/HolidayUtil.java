/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.jollyday.CalendarHierarchy;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameters;

import java.util.ArrayList;
import java.util.List;

import static de.appsolve.padelcampus.constants.Constants.NO_HOLIDAY_KEY;

/**
 * @author dominik
 */
public class HolidayUtil {

    public static List<String> getHolidayKeys() {
        List<String> holidayKeys = new ArrayList<>();
        holidayKeys.add(NO_HOLIDAY_KEY);
        for (HolidayCalendar c : HolidayCalendar.values()) {
            HolidayManager m = HolidayManager.getInstance(ManagerParameters.create(c));
            CalendarHierarchy calendarHierarchy = m.getCalendarHierarchy();
            for (String key : calendarHierarchy.getChildren().keySet()) {
                holidayKeys.add(c.name() + "-" + key);
            }
        }
        return holidayKeys;
    }

}
