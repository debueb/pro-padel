/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import static de.appsolve.padelcampus.constants.Constants.DEFAULT_TIMEZONE;
import static de.appsolve.padelcampus.constants.Constants.NO_HOLIDAY_KEY;
import de.appsolve.padelcampus.data.Court;
import de.appsolve.padelcampus.data.TimeSlot;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.CalendarConfigDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE;
import static de.appsolve.padelcampus.utils.FormatUtils.TIME_HUMAN_READABLE;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class BookingUtil {
    
    private static final Logger log = Logger.getLogger(BookingUtil.class);

    @Autowired
    Msg msg;
    
    @Autowired
    BookingDAOI bookingDAO;
    
    @Autowired
    CalendarConfigDAOI calendarConfigDAO;
    
    public List<TimeSlot> getTimeSlotsForDateAndCalendarConfigs(LocalDate selectedDate, List<CalendarConfig> calendarConfigs, Boolean onlyFutureTimeSlots){
        LocalDate today = new LocalDate(DEFAULT_TIMEZONE);
        
        //sort all calendar configurations for selected date by priority
        Collections.sort(calendarConfigs);
        
        //generate list of bookable time slots
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (CalendarConfig config : calendarConfigs) {
            LocalTime time = config.getStartTime();
            LocalTime now = new LocalTime(DEFAULT_TIMEZONE);
            
            while (time.plusMinutes(config.getMinDuration()).compareTo(config.getEndTime()) <= 0) {
                if (onlyFutureTimeSlots) {
                    if (selectedDate.isAfter(today) || time.isAfter(now)){
                        addTimeSlot(timeSlots, selectedDate, time, config);
                    }
                } else {
                    addTimeSlot(timeSlots, selectedDate, time, config);
                }
                time = time.plusMinutes(config.getMinInterval());
            }
        }
        //sort time slots by time
        Collections.sort(timeSlots);
        
        //decrease court count for every blocking booking
        List<Booking> confirmedBookings = bookingDAO.findBlockedBookingsForDate(selectedDate);
        for (TimeSlot timeSlot : timeSlots) {
            checkForBookedCourts(timeSlot, confirmedBookings);
        }
        return timeSlots;
    }
    
    private void addTimeSlot(List<TimeSlot> timeSlots, LocalDate date, LocalTime time, CalendarConfig config) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setDate(date);
        timeSlot.setStartTime(time);
        timeSlot.setEndTime(time.plusMinutes(config.getMinDuration()));
        timeSlot.setConfig(config);
        
        //only add timeSlot if timeSlots does not already contain an entry that overlaps
        if (!overlaps(timeSlot, timeSlots)){
            timeSlots.add(timeSlot);
        }
    }
    
    private boolean overlaps(TimeSlot timeSlot, List<TimeSlot> timeSlots) {
        for (TimeSlot slot: timeSlots){
            if (timeSlot.getStartTime().equals(slot.getStartTime())){
                return true;
            } else if (timeSlot.getStartTime().isBefore(slot.getStartTime())){
                //make sure timeSlot ends before slot starts
                if (timeSlot.getEndTime().isAfter(slot.getStartTime())){
                    return true;
                }
            } else {
                //only test if these time slots come from different configurations as we DO want overlapping time slots (when min interval is smaller than min duration)
                if (!timeSlot.getConfig().equals(slot.getConfig())){
                    
                    //if timeSlot starts after slot, make sure it also starts before slot ends
                    if (timeSlot.getStartTime().isBefore(slot.getEndTime())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Integer getCourtNumber(Booking booking) throws Exception {
        CalendarConfig calendarConfig = calendarConfigDAO.findFor(booking.getBookingDate(), booking.getBookingTime());
        if (calendarConfig == null){
            throw new Exception(msg.get("UnableToFindCalendarConfigForBooking", new Object[]{booking.getBookingDate().toString(DATE_HUMAN_READABLE), booking.getBookingTime().toString(TIME_HUMAN_READABLE)}));
        }
        List<Booking> confirmedBookings = bookingDAO.findBlockedBookingsForDate(booking.getBookingDate());
        List<TimeSlot> timeSlots = getTimeSlotsForDateAndCalendarConfigs(booking.getBookingDate(), Arrays.asList(new CalendarConfig[]{calendarConfig}), true);
        for (TimeSlot timeSlot: timeSlots){
            
            //if this timeslot starts at the desired booking time
            if (timeSlot.getStartTime().equals(booking.getBookingTime())){
                if (timeSlot.getFreeCourtCount()>0){
                    
                    //determine max timeSlotLatestEndTime
                    LocalTime timeSlotLatestEndTime = booking.getBookingEndTime();

                    //as long as the timeSlotLatestEndTime is before the end time configured in the calendar
                    while (timeSlotLatestEndTime.compareTo(calendarConfig.getEndTime()) <= 0) {

                        checkForBookedCourts(timeSlot, confirmedBookings);

                        //stop if there are no courts available for the requested duration.
                        //only contiguous bookings are supported
                        if (timeSlot.getFreeCourtCount()<1){
                            break;
                        }
                        timeSlotLatestEndTime = timeSlotLatestEndTime.plusMinutes(calendarConfig.getMinInterval());
                    }

                    //if the timeslot lasts at least the desired booking duration
                    if (timeSlotLatestEndTime.compareTo(booking.getBookingEndTime()) >= 0){

                        //determine courtNumber
                        //this prevents the same court from being chosen twice if a booking gets deleted or cancelled
                        Map<Integer, Court> courtMap = timeSlot.getCourtMap();
                        for (Map.Entry<Integer, Court> entrySet : courtMap.entrySet()) {
                            Court court = entrySet.getValue();
                            if (!court.getBlocked()){
                                int courtNumber = entrySet.getKey();
                                return courtNumber;
                            }
                        }
                    }
                }
            }
        }
        throw new Exception(msg.get("NoCourtAvailableFor", new Object[]{booking.getBookingDate().toString(FormatUtils.DATE_HUMAN_READABLE), booking.getBookingTime().toString(FormatUtils.TIME_HUMAN_READABLE), booking.getBookingEndTime().toString(FormatUtils.TIME_HUMAN_READABLE)}));
    }

    public void checkForBookedCourts(TimeSlot timeSlot, List<Booking> confirmedBookings) {
        LocalTime selectedTime = timeSlot.getStartTime();
        LocalTime endTime = timeSlot.getEndTime();
        Map<Integer, Court> courtMap = timeSlot.getCourtMap();

        for (Booking booking : confirmedBookings) {
            LocalTime bookingStartTime = booking.getBookingTime();
            LocalTime bookingEndTime = bookingStartTime.plusMinutes(booking.getDuration().intValue());

            if (selectedTime.isBefore(bookingEndTime)) {
                if (endTime.isAfter(bookingStartTime)) {

                    Court court = courtMap.get(booking.getCourtNumber());
                    court.setBlocked(true);

                    //log.info(booking.getBookingTime()+" - "+booking.getBookingEndTime()+" Court: "+booking.getCourtNumber());
                    //log.info(timeSlot.getStartTime()+" - "+timeSlot.getEndTime()+" Free:  "+timeSlot.getFreeCourtCount());

                    timeSlot.addBooking(booking);
                }
            }
        }
    }
    
    public boolean isHoliday(LocalDate date, CalendarConfig calendarConfig) {
        String holidayKey = calendarConfig.getHolidayKey();
        boolean isHoliday = false;
        if (!holidayKey.equals(NO_HOLIDAY_KEY)) {
            String[] holidayKeySplit = holidayKey.split("-");
            String country = holidayKeySplit[0];
            String region = holidayKeySplit[1];

            HolidayManager countryHolidays = HolidayManager.getInstance(HolidayCalendar.valueOf(country));
            isHoliday = countryHolidays.isHoliday(date, region);
        }
        return isHoliday;
    }
    
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
