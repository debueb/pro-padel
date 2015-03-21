/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

import de.appsolve.padelcampus.constants.Currency;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.db.model.Offer;
import java.math.BigDecimal;
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
    
    private ArrayList<CalendarConfig> configs;
    
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

    public ArrayList<CalendarConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(ArrayList<CalendarConfig> configs) {
        this.configs = configs;
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
        for (Offer offer: getOffers()){
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
    
    
    //convenience methods
    public List<Offer> getOffers(){
        List<Offer> offers = new ArrayList<>();
        for (CalendarConfig config: getConfigs()){
            offers.addAll(config.getOffers());
        }
        return offers;
    }
    
    private CalendarConfig getPrimaryConfig(){
        BigDecimal max = BigDecimal.ZERO;
        CalendarConfig primaryConfig = getConfigs().get(0);
        for (CalendarConfig config: getConfigs()){
            if (config.getBasePrice().compareTo(max) == 1){
                primaryConfig = config;
            }
        }
        return primaryConfig;
    }
    
    public Currency getCurrency(){
        return getPrimaryConfig().getCurrency();
    }
    
    public BigDecimal getBasePrice(){
        return getPrimaryConfig().getBasePrice();
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
