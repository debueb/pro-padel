/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.comparators;

import de.appsolve.padelcampus.db.model.CalendarConfig;
import java.util.Comparator;

/**
 *
 * @author dominik
 */
public class CalendarConfigStartTimeComparator implements Comparator<CalendarConfig> {

    @Override
    public int compare(CalendarConfig o1, CalendarConfig o2) {
        return o1.getStartTime().compareTo(o2.getStartTime());
    }
    
}
