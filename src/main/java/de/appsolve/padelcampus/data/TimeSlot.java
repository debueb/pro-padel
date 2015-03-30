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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    
    //jstl methods
    public Map<CalendarConfig, List<Offer>> getConfigOfferMap(){
        Map<CalendarConfig, List<Offer>> map = new HashMap<>();
        List<CalendarConfig> configs = getConfigs();
        Collections.sort(configs);
        for (CalendarConfig config: configs){
            for (Offer offer: config.getOffers()){
                if (getFreeCourtCount(offer)>0){
                    List<Offer> offers = map.get(config);
                    if (offers==null){
                        offers = new ArrayList<>();
                    }
                    offers.add(offer);
                    map.put(config, offers);
                }
            }
        }
        return map;
    }
    
    private Long getFreeCourtCount(Offer offer){
        Long freeCourtCount = offer.getMaxConcurrentBookings();
        for (Booking booking: getBookings()){
            if (booking.getOffer().equals(offer)){
                freeCourtCount -= 1;
                break;
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
    
    @Override
    public int compareTo(TimeSlot o) {
        return getStartTime().compareTo(o.getStartTime());
    }
    
    @Override 
    public String toString(){
        return date.toString("EEE")+" "+getStartTime()+" - "+getEndTime()+": "+getOffers();
    }
}
