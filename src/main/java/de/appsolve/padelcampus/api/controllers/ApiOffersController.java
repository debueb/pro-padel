/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.api.controllers;

import de.appsolve.padelcampus.data.TimeSlot;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.CalendarConfigDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.db.model.Offer;
import de.appsolve.padelcampus.exceptions.CalendarConfigException;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import de.appsolve.padelcampus.utils.BookingUtil;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author dominik
 */
@Controller
@RequestMapping("/api/offers")
public class ApiOffersController{
    
    @Autowired
    BookingUtil bookingUtil;
    
    @Autowired
    CalendarConfigDAOI calendarConfigDAO;

    @Autowired
    BookingDAOI bookingDAO;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
    }

    @RequestMapping(value="{date}/{time}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Set<ApiOffer> getAvailableOffers(@PathVariable("date") LocalDate date, @PathVariable("time") LocalTime time) throws CalendarConfigException{
        List<CalendarConfig> configs = calendarConfigDAO.findFor(date);
        List<Booking> confirmedBookings = bookingDAO.findBlockedBookingsForDate(date);
        List<TimeSlot> timeSlots = bookingUtil.getTimeSlotsForDate(date, configs, confirmedBookings, Boolean.TRUE, Boolean.TRUE);
        Set<ApiOffer> offers = new HashSet<>();
        for (TimeSlot timeSlot: timeSlots){
            if (timeSlot.getStartTime().equals(time)){
                for (Offer offer: timeSlot.getAvailableOffers()){
                    ApiOffer apiOffer = new ApiOffer();
                    apiOffer.setName(offer.getName());
                    offers.add(apiOffer);
                }
            }
        }
        return offers;
    }
}
