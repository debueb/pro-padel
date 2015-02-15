/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.admin.controller.reports;

import de.appsolve.padelcampus.constants.CalendarWeekDay;
import java.util.Objects;

/**
 *
 * @author dominik
 */
public class TimeHeatMapEntry implements Comparable<TimeHeatMapEntry>{
    
    private CalendarWeekDay dayOfWeek;
    
    private String time;
    
    private Integer count;

    public CalendarWeekDay getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(CalendarWeekDay dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.dayOfWeek);
        hash = 97 * hash + Objects.hashCode(this.time);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TimeHeatMapEntry other = (TimeHeatMapEntry) obj;
        if (!Objects.equals(this.dayOfWeek, other.dayOfWeek)) {
            return false;
        }
        return Objects.equals(this.time, other.time);
    }

    @Override
    public int compareTo(TimeHeatMapEntry o) {
        int result = time.compareTo(o.time);
        if (result == 0){
            result = dayOfWeek.compareTo(o.dayOfWeek);
        }
        return result;
    }
}
