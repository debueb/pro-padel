/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.db.model.Offer;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author dominik
 */
public class TimeSlot implements Comparable<TimeSlot> {

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private CalendarConfig config;

    private List<Booking> bookings;

    private BigDecimal pricePerMinDuration;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

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

    public CalendarConfig getConfig() {
        return config;
    }

    public void setConfig(CalendarConfig config) {
        this.config = config;
    }

    public List<Booking> getBookings() {
        return bookings == null ? new ArrayList<Booking>() : bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public void addBooking(Booking booking) {
        List<Booking> localBookings = getBookings();
        localBookings.add(booking);
        //sort bookings by booking time (needed by UI)
        Collections.sort(localBookings);
        setBookings(localBookings);
    }

    public BigDecimal getPricePerMinDuration() {
        return pricePerMinDuration;
    }

    public void setPricePerMinDuration(BigDecimal pricePerMinDuration) {
        this.pricePerMinDuration = pricePerMinDuration;
    }

    //jstl
    public List<Offer> getAvailableOffers() {
        List<Offer> sortedOffers = new ArrayList<>();
        if (config != null) {
            sortedOffers.addAll(config.getOffers());
        }
        Collections.sort(sortedOffers);
        Iterator<Offer> iterator = sortedOffers.iterator();
        while (iterator.hasNext()) {
            Offer offer = iterator.next();
            if (getFreeCourtCount(offer) <= 0) {
                iterator.remove();
            }
        }
        return sortedOffers;
    }

    private Long getFreeCourtCount(Offer offer) {
        Long freeCourtCount = offer.getMaxConcurrentBookings();
        for (Booking booking : getBookings()) {
            if (booking.getOffer().equals(offer)) {
                freeCourtCount -= 1;
                break;
            }
        }
        return freeCourtCount;
    }

    @Override
    public int compareTo(TimeSlot o) {
        return getStartTime().compareTo(o.getStartTime());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.startTime);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TimeSlot other = (TimeSlot) obj;
        if (!Objects.equals(this.startTime, other.startTime)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getDate().toString("EEE") + " " + getStartTime() + " - " + getEndTime();
    }
}
