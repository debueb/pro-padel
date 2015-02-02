/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.db.model.Offer;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 *
 * @author dominik
 */
public class TimeSlot implements Comparable<TimeSlot>{
    
    private LocalDate date;
    
    private LocalTime startTime;
    
    private LocalTime endTime;
    
    private CalendarConfig config;
    
    private List<Booking> bookings;

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
    
    public void addBooking(Booking booking){
        List<Booking> _bookings = getBookings();
        _bookings.add(booking);
        setBookings(_bookings);
    }
    
    public Long getFreeCourtCount(){
        Long freeCourtCount = 0L;
        for (Offer offer: getConfig().getOffers()){
            freeCourtCount += offer.getMaxConcurrentBookings();
            for (Booking booking: getBookings()){
                if (booking.getOffer().equals(offer)){
                    freeCourtCount -= 1;
                    break;
                }
            }
        }
        return freeCourtCount;
    }
    
    @Override
    public int compareTo(TimeSlot o) {
        return getStartTime().compareTo(o.getStartTime());
    }
    
    @Override 
    public String toString(){
        return getStartTime()+" - "+getEndTime();
    }
}
