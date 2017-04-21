/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.api.controllers;

import de.appsolve.padelcampus.constants.OfferOptionType;
import de.appsolve.padelcampus.db.dao.BookingBaseDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.OfferOption;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/bookings")
public class ApiBookingsController {
    
    private static final Logger LOG = Logger.getLogger(ApiBookingsController.class);

    @Autowired
    BookingBaseDAOI bookingBaseDAO;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
    }

    @RequestMapping("/{date}/{time}/video")
    @ResponseBody
    public List<Booking> getVideoBookings(@PathVariable("date") LocalDate date, @PathVariable("time") LocalTime time) {
        LOG.info(String.format("Looking for bookings eligible to record at %s", time));
        List<Booking> bookings = bookingBaseDAO.findCurrentBookingsWithOfferOptions(date, time);
        LOG.info(String.format("Found %s bookings eligible for video recording", bookings.size()));

        List<Booking> videoBookings = new ArrayList<>();
        for (Booking booking: bookings){
            if (booking.getOfferOptions() != null) {
                for (OfferOption offerOption : booking.getOfferOptions()) {
                    if (offerOption.getOfferOptionType().equals(OfferOptionType.VideoRecording)) {
                        Booking videoBooking = new Booking();
                        videoBooking.setUUID(booking.getUUID());
                        videoBooking.setDuration(booking.getDuration());
                        videoBookings.add(videoBooking);
                    }
                }
            }
        }
        return videoBookings;
    }
}
    
            
