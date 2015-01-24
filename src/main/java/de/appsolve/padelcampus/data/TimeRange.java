/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.data;

import java.util.Objects;
import org.joda.time.LocalTime;

/**
 *
 * @author dominik
 */
public class TimeRange implements Comparable<TimeRange> {
    
    private LocalTime startTime;
    
    private LocalTime endTime;

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.startTime);
        hash = 31 * hash + Objects.hashCode(this.endTime);
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
        final TimeRange other = (TimeRange) obj;
        if (!Objects.equals(this.startTime, other.startTime)) {
            return false;
        }
        return Objects.equals(this.endTime, other.endTime);
    }

    @Override
    public int compareTo(TimeRange o) {
        return this.startTime.compareTo(o.startTime);
    }
}
