/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.constants.CalendarWeekDay;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import javax.persistence.*;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import static de.appsolve.padelcampus.utils.FormatUtils.TIME_HUMAN_READABLE;

/**
 * @author dominik
 */
@Entity
public class Voucher extends CustomerEntity {

    @Transient
    private static final long serialVersionUID = 1L;

    @Column
    private String UUID;

    @Column(length = 4000)
    private String comment;

    @Column
    private Long duration;

    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate validUntil;

    @Column
    private Integer validFromHour;

    @Column
    private Integer validFromMinute;

    @Column
    private Integer validUntilHour;

    @Column
    private Integer validUntilMinute;

    @Column
    private Boolean used;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @NotEmpty(message = "{NotEmpty.calendarWeekDays}")
    private Set<CalendarWeekDay> calendarWeekDays;

    @ManyToMany(fetch = FetchType.EAGER)
    @NotEmpty(message = "{NotEmpty.offers}")
    private Set<Offer> offers;

    @OneToOne
    private Game game;

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public Boolean getUsed() {
        return used == null ? Boolean.FALSE : used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getValidFromHour() {
        return validFromHour;
    }

    public void setValidFromHour(Integer validFromHour) {
        this.validFromHour = validFromHour;
    }

    public Integer getValidFromMinute() {
        return validFromMinute;
    }

    public void setValidFromMinute(Integer validFromMinute) {
        this.validFromMinute = validFromMinute;
    }

    public Integer getValidUntilHour() {
        return validUntilHour;
    }

    public void setValidUntilHour(Integer validUntilHour) {
        this.validUntilHour = validUntilHour;
    }

    public Integer getValidUntilMinute() {
        return validUntilMinute;
    }

    public void setValidUntilMinute(Integer validUntilMinute) {
        this.validUntilMinute = validUntilMinute;
    }

    public LocalTime getValidFromTime() {
        return TIME_HUMAN_READABLE.parseLocalTime(getValidFromHour() + ":" + getValidFromMinute());
    }

    public void setValidFromTime(LocalTime time) {
        this.validFromHour = time.getHourOfDay();
        this.validFromMinute = time.getMinuteOfHour();
    }

    public LocalTime getValidUntilTime() {
        return TIME_HUMAN_READABLE.parseLocalTime(getValidUntilHour() + ":" + getValidUntilMinute());
    }

    public void setValidUntilTime(LocalTime time) {
        this.validUntilHour = time.getHourOfDay();
        this.validUntilMinute = time.getMinuteOfHour();
    }

    public Set<CalendarWeekDay> getCalendarWeekDays() {
        if (calendarWeekDays != null && !calendarWeekDays.isEmpty()) {
            return EnumSet.copyOf(calendarWeekDays);
        }
        return Collections.<CalendarWeekDay>emptySet();
    }

    public void setCalendarWeekDays(Set<CalendarWeekDay> calendarWeekDays) {
        this.calendarWeekDays = calendarWeekDays;
    }

    public Set<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return getUUID() + " (" + getComment() + ")";
    }
}
