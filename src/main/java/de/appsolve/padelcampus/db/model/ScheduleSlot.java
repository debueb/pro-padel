/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Type;
import org.joda.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

/**
 * @author dominik
 */
@Entity
public class ScheduleSlot extends ComparableEntity {

    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
    @JsonFormat(shape = STRING, pattern = "HH:mm")
    private LocalTime startTime;

    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
    @JsonFormat(shape = STRING, pattern = "HH:mm")
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
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.getId()) + Objects.hashCode(this.getStartTime()) + Objects.hashCode(this.getEndTime());
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
        final ScheduleSlot other = (ScheduleSlot) obj;
        return Objects.equals(this.getId(), other.getId()) && this.getStartTime().equals(other.getStartTime()) && this.getEndTime().equals(other.getEndTime());
    }
}
