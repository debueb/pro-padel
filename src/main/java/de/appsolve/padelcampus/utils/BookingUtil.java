/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.appsolve.padelcampus.constants.CalendarWeekDay;
import de.appsolve.padelcampus.constants.Constants;
import static de.appsolve.padelcampus.constants.Constants.DEFAULT_TIMEZONE;
import static de.appsolve.padelcampus.constants.Constants.NO_HOLIDAY_KEY;
import de.appsolve.padelcampus.data.DatePickerDayConfig;
import de.appsolve.padelcampus.data.TimeRange;
import de.appsolve.padelcampus.data.TimeSlot;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.CalendarConfigDAOI;
import de.appsolve.padelcampus.db.dao.FacilityDAOI;
import de.appsolve.padelcampus.db.dao.OfferDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.db.model.Facility;
import de.appsolve.padelcampus.db.model.Offer;
import de.appsolve.padelcampus.exceptions.CalendarConfigException;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameters;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

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
    OfferDAOI offerDAO;
    
    @Autowired
    FacilityDAOI facilityDAO;
    
    @Autowired
    BookingDAOI bookingDAO;
    
    @Autowired
    CalendarConfigDAOI calendarConfigDAO;
    
    @Autowired
    CalendarConfigUtil calendarConfigUtil;
    
    @Autowired
    ObjectMapper objectMapper;
    
    public List<TimeSlot> getTimeSlotsForDate(LocalDate selectedDate, List<CalendarConfig> allCalendarConfigs, List<Booking> existingBookings, Boolean onlyFutureTimeSlots, Boolean preventOverlapping) throws CalendarConfigException{
        
        List<CalendarConfig> calendarConfigs = calendarConfigUtil.getCalendarConfigsMatchingDate(allCalendarConfigs, selectedDate);
        Iterator<CalendarConfig> iterator = calendarConfigs.iterator();
        while(iterator.hasNext()){
            CalendarConfig calendarConfig = iterator.next();
            if (isHoliday(selectedDate, calendarConfig)) {
                iterator.remove();
            }
        }
        
        List<TimeSlot> timeSlots = new ArrayList<>();
        if (calendarConfigs.size()>0){
            LocalDate today = new LocalDate(DEFAULT_TIMEZONE);

            //sort all calendar configurations for selected date by start time
            Collections.sort(calendarConfigs);

            CalendarConfig previousConfig = null;
            LocalDateTime time = null;
            LocalDateTime now = new LocalDateTime(DEFAULT_TIMEZONE);
            
            //generate list of bookable time slots
            for (CalendarConfig config : calendarConfigs) {
                LocalDateTime startDateTime = getLocalDateTime(selectedDate, config.getStartTime());
                if (time==null){
                    //on first iteration
                    time = startDateTime;
                } else {
                    if (time.plusMinutes(previousConfig.getMinInterval()).equals(startDateTime)){
                        //contiguous bookings possible
                        //time = time;
                    } else {
                        //reset basePriceLastConfig as this is a non contiguous offer
                        previousConfig = null;
                        time = startDateTime;
                    }
                }
                LocalDateTime endDateTime = getLocalDateTime(selectedDate, config.getEndTime());
                while (time.plusMinutes(config.getMinDuration()).compareTo(endDateTime) <= 0) {
                    BigDecimal pricePerMinDuration;
                    if (previousConfig == null){
                        pricePerMinDuration = config.getBasePrice();
                    } else {
                        BigDecimal previousConfigBasePricePerMinute = getPricePerMinute(previousConfig);
                        pricePerMinDuration = previousConfigBasePricePerMinute.multiply(new BigDecimal(previousConfig.getMinInterval()), MathContext.DECIMAL128);
                        BigDecimal basePricePerMinute = getPricePerMinute(config);
                        pricePerMinDuration = pricePerMinDuration.add(basePricePerMinute.multiply(new BigDecimal(config.getMinDuration()-previousConfig.getMinInterval()), MathContext.DECIMAL128));
                        previousConfig = null;
                    }
                    pricePerMinDuration = pricePerMinDuration.setScale(2, RoundingMode.HALF_EVEN);
                    if (onlyFutureTimeSlots) {
                        if (selectedDate.isAfter(today) || time.isAfter(now)){
                            addTimeSlot(timeSlots, time, config, pricePerMinDuration);
                        }
                    } else {
                        addTimeSlot(timeSlots, time, config, pricePerMinDuration);
                    }
                    time = time.plusMinutes(config.getMinInterval());
                }
                previousConfig = config;
            }
            //sort time slots by time
            Collections.sort(timeSlots);

            //decrease court count for every blocking booking
            for (TimeSlot timeSlot : timeSlots) {
                checkForBookedCourts(timeSlot, existingBookings, preventOverlapping);
            }
        }
        return timeSlots;
    }
    
    private void addTimeSlot(List<TimeSlot> timeSlots, LocalDateTime time, CalendarConfig config, BigDecimal pricePerMinDuration) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setDate(time.toLocalDate());
        timeSlot.setStartTime(time.toLocalTime());
        timeSlot.setEndTime(time.toLocalTime().plusMinutes(config.getMinDuration()));
        timeSlot.setConfig(config);
        timeSlot.setPricePerMinDuration(pricePerMinDuration);

        //only add timeSlot if timeSlots does not already contain an entry that overlaps
        if (!overlaps(timeSlot, timeSlots)){
            timeSlots.add(timeSlot);
        }
    }
    
    private boolean overlaps(TimeSlot timeSlot, List<TimeSlot> timeSlots) {
        for (TimeSlot slot: timeSlots){
            if (timeSlot.getStartTime().equals(slot.getStartTime())){
                return configMatches(timeSlot, slot);
            } else if (timeSlot.getStartTime().isBefore(slot.getStartTime())){
                //make sure timeSlot ends before slot starts
                if (timeSlot.getEndTime().isAfter(slot.getStartTime())){
                    return configMatches(timeSlot, slot);
                }
            } else {
                //disabled, as we do want overlapping time slots from different calendar configurations
//                //only test if these time slots come from different configurations as we DO want overlapping time slots (when min interval is smaller than min duration)
//                if (!timeSlot.getConfig().equals(slot.getConfig())){
//                    
//                    //if timeSlot starts after slot, make sure it also starts before slot ends
//                    if (timeSlot.getStartTime().isBefore(slot.getEndTime())){
//                        return true;
//                    }
//                }
            }
        }
        return false;
    }
    
    private boolean configMatches(TimeSlot timeSlot, TimeSlot slot){
        return timeSlot.getConfig().equals(slot.getConfig());
    }

    public void checkForBookedCourts(TimeSlot timeSlot, List<Booking> confirmedBookings, Boolean preventOverlapping) {
        LocalTime startTime = timeSlot.getStartTime();
        LocalTime endTime = timeSlot.getEndTime();
        
        for (Booking booking : confirmedBookings) {
            
            if (timeSlot.getDate().equals(booking.getBookingDate())){
                LocalTime bookingStartTime = booking.getBookingTime();
                LocalTime bookingEndTime = bookingStartTime.plusMinutes(booking.getDuration().intValue());
                Boolean addBooking = false;
                if (preventOverlapping){
                    if (startTime.isBefore(bookingEndTime)) {
                        if (endTime.isAfter(bookingStartTime)) {
                            addBooking = true;
                        }
                    }
                } else {
                    //for displaying allocations
                    if (startTime.compareTo(bookingStartTime)>=0){
                        if (endTime.compareTo(bookingEndTime)<=0){
                            addBooking = true;
                        }
                    }
                }
                if (addBooking){
                    Offer offer = booking.getOffer();
                    for (Offer timeSlotOffer: timeSlot.getConfig().getOffers()){
                        if (offer.equals(timeSlotOffer)){
                            timeSlot.addBooking(booking);
                            break;
                        }
                    }
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

            HolidayManager countryHolidays = HolidayManager.getInstance(ManagerParameters.create(HolidayCalendar.valueOf(country)));
            isHoliday = countryHolidays.isHoliday(date, region);
        }
        return isHoliday;
    }
    
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public void addWeekView(LocalDate selectedDate, List<Facility> selectedFacilities, ModelAndView mav, Boolean preventOverlapping) throws JsonProcessingException {
        //calculate date configuration for datepicker
        LocalDate today = new LocalDate(DEFAULT_TIMEZONE);
        LocalDate firstDay = today.dayOfMonth().withMinimumValue();
        LocalDate lastDay = today.plusDays(Constants.CALENDAR_MAX_DATE).dayOfMonth().withMaximumValue();
        List<CalendarConfig> calendarConfigs = calendarConfigDAO.findBetween(firstDay, lastDay);
        Collections.sort(calendarConfigs);
        Map<String, DatePickerDayConfig> dayConfigs = getDayConfigMap(firstDay, lastDay, calendarConfigs);
        
        List<Booking> confirmedBookings = bookingDAO.findBlockedBookingsBetween(firstDay, lastDay);
        
        //calculate available time slots
        List<TimeSlot> timeSlots = new ArrayList<>();
        List<LocalDate> weekDays = new ArrayList<>();
        for (int i=1; i<=CalendarWeekDay.values().length; i++){
            LocalDate date = selectedDate.withDayOfWeek(i);
            weekDays.add(date);
            if (!date.isBefore(new LocalDate())){
                try {
                    //generate list of bookable time slots
                    timeSlots.addAll(getTimeSlotsForDate(date, calendarConfigs, confirmedBookings, true, preventOverlapping));
                } catch (CalendarConfigException e){
                    //safe to ignore
                }
            }
        }
        
        SortedSet<Offer> offers = new TreeSet<>();
        List<TimeRange> rangeList = new ArrayList<>();
        //Map<TimeRange, List<TimeSlot>> rangeList = new TreeMap<>();
        for (TimeSlot slot: timeSlots){
            Set<Offer> slotOffers = slot.getConfig().getOffers();
            offers.addAll(slotOffers);
            
            TimeRange range = new TimeRange();
            range.setStartTime(slot.getStartTime());
            range.setEndTime(slot.getEndTime());

            if (rangeList.contains(range)){
                range = rangeList.get(rangeList.indexOf(range));
            } else {
                rangeList.add(range);
            }

            List<TimeSlot> slotis = range.getTimeSlots();
            slotis.add(slot);
            range.setTimeSlots(slotis);
        }
        Collections.sort(rangeList);
        
        List<Offer> selectedOffers = new ArrayList<>();
        if (selectedFacilities.isEmpty()){
            selectedOffers = offerDAO.findAll();
        } else {
            for (Facility facility: selectedFacilities){
                selectedOffers.addAll(facility.getOffers());
            }
        }
        Collections.sort(selectedOffers);
        
        mav.addObject("dayConfigs", objectMapper.writeValueAsString(dayConfigs));
        mav.addObject("maxDate", lastDay.toString());
        mav.addObject("Day", selectedDate);
        mav.addObject("WeekDays", weekDays);
        mav.addObject("RangeMap", rangeList);
        mav.addObject("Offers", offers);
        mav.addObject("SelectedOffers", selectedOffers);
        mav.addObject("SelectedFacilities", selectedFacilities);
        mav.addObject("Facilities", facilityDAO.findAll());
    }

    public Long getBookingSlotsLeft(TimeSlot timeSlot, Offer offer, List<Booking> confirmedBookings) {
        checkForBookedCourts(timeSlot, confirmedBookings, true);

        Long bookingSlotsLeft = offer.getMaxConcurrentBookings();
        for (Booking existingBooking: timeSlot.getBookings()){
            if (existingBooking.getOffer().equals(offer)){
                bookingSlotsLeft--;
            }
        }
        return bookingSlotsLeft;
    }

    public Map<String, DatePickerDayConfig> getDayConfigMap(LocalDate firstDay, LocalDate lastDay, List<CalendarConfig> calendarConfigs) {
        Map<String, DatePickerDayConfig> dayConfigs = new HashMap<>();
        for (LocalDate date = firstDay; date.isBefore(lastDay); date = date.plusDays(1)) {
            DatePickerDayConfig dayConfig = new DatePickerDayConfig();
            dayConfig.setSelectable(Boolean.FALSE);
            for (CalendarConfig calendarConfig : calendarConfigs) {
                for (CalendarWeekDay weekDay : calendarConfig.getCalendarWeekDays()) {
                    if (weekDay.ordinal() + 1 == date.getDayOfWeek()) {
                        if (!isHoliday(date, calendarConfig)) {
                            if (calendarConfig.getStartDate().compareTo(date) <= 0 && calendarConfig.getEndDate().compareTo(date) >= 0) {
                                dayConfig.setSelectable(Boolean.TRUE);
                            }
                        }
                    }
                }
            }
            dayConfigs.put(date.toString(), dayConfig);
        }
        return dayConfigs;
    }

    public LocalDateTime getLocalDateTime(LocalDate selectedDate, LocalTime startTime) {
        return new LocalDateTime(selectedDate.getYear(), selectedDate.getMonthOfYear(), selectedDate.getDayOfMonth(), startTime.getHourOfDay(), startTime.getMinuteOfHour());
    }

    public BigDecimal getPricePerMinute(CalendarConfig config) {
        return config.getBasePrice().divide(new BigDecimal(config.getMinDuration().toString()), MathContext.DECIMAL128);
    }
}
