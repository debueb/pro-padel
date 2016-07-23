/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.constants.CalendarWeekDay;
import de.appsolve.padelcampus.constants.Currency;
import de.appsolve.padelcampus.constants.PaymentMethod;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_MEDIUM;
import static de.appsolve.padelcampus.utils.FormatUtils.TIME_HUMAN_READABLE;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.SortNatural;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 *
 * @author dominik
 */
@Entity
public class CalendarConfig extends CustomerEntity{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @Column
    @ElementCollection(fetch=FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @NotEmpty(message = "{NotEmpty.calendarWeekDays}")
    private Set<CalendarWeekDay> calendarWeekDays;
    
    @Column
    @ElementCollection(fetch=FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @NotEmpty(message = "{NotEmpty.paymentMethods}")
    private Set<PaymentMethod> paymentMethods;
    
    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate startDate;
    
    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate endDate;
    
    @Column
    private Integer startTimeHour;
    
    @Column
    private Integer startTimeMinute;
    
    @Column
    private Integer endTimeHour;
    
    @Column
    private Integer endTimeMinute;
    
    @ManyToMany(fetch=FetchType.EAGER)
    @NotEmpty(message = "{NotEmpty.offers}")
    @SortNatural
    private Set<Offer> offers;
    
    @Column
    private Integer minDuration;
    
    @Column
    private Integer minInterval;
    
    @Column
    @NotNull(message = "{NotEmpty.price}")
    private BigDecimal basePrice;
    
    @Column
    @Enumerated(EnumType.STRING)
    private Currency currency;
    
    @Column
    private String holidayKey;

    public Set<CalendarWeekDay> getCalendarWeekDays() {
        if (calendarWeekDays!=null && !calendarWeekDays.isEmpty()){
            return EnumSet.copyOf(calendarWeekDays);
        }
        return Collections.EMPTY_SET;
    }

    public void setCalendarWeekDays(Set<CalendarWeekDay> calendarWeekDays) {
        this.calendarWeekDays = calendarWeekDays;
    }

    public Set<PaymentMethod> getPaymentMethods() {
        if (paymentMethods!=null && !paymentMethods.isEmpty()){
            return EnumSet.copyOf(paymentMethods);
        }
        return Collections.EMPTY_SET;
    }

    public void setPaymentMethods(Set<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getStartTimeHour() {
        return startTimeHour;
    }

    public void setStartTimeHour(Integer startTimeHour) {
        this.startTimeHour = startTimeHour;
    }

    public Integer getStartTimeMinute() {
        return startTimeMinute;
    }

    public void setStartTimeMinute(Integer startTimeMinute) {
        this.startTimeMinute = startTimeMinute;
    }
    
    public LocalTime getStartTime(){
        return TIME_HUMAN_READABLE.parseLocalTime(getStartTimeHour()+":"+getStartTimeMinute());
    }

    public Integer getEndTimeHour() {
        return endTimeHour;
    }

    public void setEndTimeHour(Integer endTimeHour) {
        this.endTimeHour = endTimeHour;
    }

    public Integer getEndTimeMinute() {
        return endTimeMinute;
    }

    public void setEndTimeMinute(Integer endTimeMinute) {
        this.endTimeMinute = endTimeMinute;
    }
    
    public LocalTime getEndTime(){
        return TIME_HUMAN_READABLE.parseLocalTime(getEndTimeHour()+":"+getEndTimeMinute());
    }

    public Set<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }

    public Integer getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(Integer minDuration) {
        this.minDuration = minDuration;
    }

    public Integer getMinInterval() {
        return minInterval;
    }

    public void setMinInterval(Integer minInterval) {
        this.minInterval = minInterval;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getHolidayKey() {
        return holidayKey;
    }

    public void setHolidayKey(String holidayKey) {
        this.holidayKey = holidayKey;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Offer offer: getOffers()){
            sb.append(offer).append(", ");
        }
        sb.append(startDate.toString(DATE_MEDIUM)).append(" - ").append(endDate.toString(DATE_MEDIUM)).append(": ");
        sb.append(getStartTime().toString(TIME_HUMAN_READABLE)).append(" - ").append(getEndTime().toString(TIME_HUMAN_READABLE));
        return sb.toString();
    }

    @Override
    public int compareTo(BaseEntityI o) {
        if (o instanceof CalendarConfig){
            CalendarConfig co = (CalendarConfig) o;
            return getStartTime().compareTo(co.getStartTime());
        }
        return super.compareTo(o);
    }
}
