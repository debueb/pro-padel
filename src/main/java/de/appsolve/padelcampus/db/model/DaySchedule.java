/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * @author dominik
 */
@Entity
public class DaySchedule extends ComparableEntity {

    private static final Logger LOG = Logger.getLogger(DaySchedule.class);

    /*
    0 = Monday
    ...
    6 = Sunday
    */
    @Column
    private Long weekDay;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ScheduleSlot> scheduleSlots;

    public Long getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(Long weekDay) {
        this.weekDay = weekDay;
    }

    public Set<ScheduleSlot> getScheduleSlots() {
        return scheduleSlots;
    }

    public void setScheduleSlots(Set<ScheduleSlot> scheduleSlots) {
        this.scheduleSlots = scheduleSlots;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.getId()) + Objects.hashCode(this.getWeekDay());
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
        final DaySchedule other = (DaySchedule) obj;
        return Objects.equals(this.getId(), other.getId()) && this.getWeekDay().equals(other.getWeekDay());
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JodaModule());
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            LOG.error(ex);
            return super.toString();
        }
    }


}
