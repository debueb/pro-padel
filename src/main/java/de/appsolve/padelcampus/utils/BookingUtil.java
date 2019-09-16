/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.paypal.base.codec.binary.Base64;
import de.appsolve.padelcampus.constants.*;
import de.appsolve.padelcampus.data.*;
import de.appsolve.padelcampus.db.dao.*;
import de.appsolve.padelcampus.db.model.*;
import de.appsolve.padelcampus.exceptions.CalendarConfigException;
import de.appsolve.padelcampus.exceptions.MailException;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameters;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static de.appsolve.padelcampus.constants.Constants.*;

/**
 * @author dominik
 */
@Component
public class BookingUtil {

    private static final Logger LOG = Logger.getLogger(BookingUtil.class);

    @Autowired
    Msg msg;

    @Autowired
    OfferDAOI offerDAO;

    @Autowired
    FacilityDAOI facilityDAO;

    @Autowired
    BookingDAOI bookingDAO;

    @Autowired
    ContactDAOI contactDAO;

    @Autowired
    CalendarConfigDAOI calendarConfigDAO;

    @Autowired
    CalendarConfigUtil calendarConfigUtil;

    @Autowired
    BookingMailSettingsDAOI bookingMailSettingsDAO;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MailUtils mailUtils;

    @Autowired
    PayPalConfigDAOI payPalConfigDAO;

    @Autowired
    PayDirektConfigDAOI payDirektConfigDAO;

    @Autowired
    PayMillConfigDAOI payMillConfigDAO;

    @Autowired
    SessionUtil sessionUtil;

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public List<TimeSlot> getTimeSlotsForDate(LocalDate selectedDate, List<CalendarConfig> allCalendarConfigs, List<Booking> existingBookings, Boolean onlyFutureTimeSlots, Boolean preventOverlapping) throws CalendarConfigException {

        List<CalendarConfig> calendarConfigs = calendarConfigUtil.getCalendarConfigsMatchingDate(allCalendarConfigs, selectedDate);
        Iterator<CalendarConfig> iterator = calendarConfigs.iterator();
        while (iterator.hasNext()) {
            CalendarConfig calendarConfig = iterator.next();
            if (isHoliday(selectedDate, calendarConfig)) {
                iterator.remove();
            }
        }

        List<TimeSlot> timeSlots = new ArrayList<>();
        if (calendarConfigs.size() > 0) {
            LocalDate today = new LocalDate(DEFAULT_TIMEZONE);

            //sort all calendar configurations for selected date by start time
            Collections.sort(calendarConfigs);

            CalendarConfig previousConfig = null;
            LocalDateTime time = null;
            LocalDateTime now = new LocalDateTime(DEFAULT_TIMEZONE);

            //generate list of bookable time slots
            int i = 0;
            for (CalendarConfig config : calendarConfigs) {
                i++;
                LocalDateTime startDateTime = getLocalDateTime(selectedDate, config.getStartTime());
                if (time == null) {
                    //on first iteration
                    time = startDateTime;
                } else if (!time.plusMinutes(previousConfig.getMinInterval()).equals(startDateTime)) {
                    //reset basePriceLastConfig as this is a non contiguous offer
                    previousConfig = null;
                    time = startDateTime;
                }
                LocalDateTime endDateTime = getLocalDateTime(selectedDate, config.getEndTime());
                while (time.plusMinutes(config.getMinDuration()).compareTo(endDateTime) <= 0) {
                    BigDecimal pricePerMinDuration;
                    if (previousConfig == null) {
                        pricePerMinDuration = config.getBasePrice();
                    } else {
                        BigDecimal previousConfigBasePricePerMinute = getPricePerMinute(previousConfig);
                        pricePerMinDuration = previousConfigBasePricePerMinute.multiply(new BigDecimal(previousConfig.getMinInterval()), MathContext.DECIMAL128);
                        BigDecimal basePricePerMinute = getPricePerMinute(config);
                        pricePerMinDuration = pricePerMinDuration.add(basePricePerMinute.multiply(new BigDecimal(config.getMinDuration() - previousConfig.getMinInterval()), MathContext.DECIMAL128));
                        previousConfig = null;
                    }
                    pricePerMinDuration = pricePerMinDuration.setScale(2, RoundingMode.HALF_EVEN);
                    if (onlyFutureTimeSlots) {
                        if (selectedDate.isAfter(today) || time.isAfter(now)) {
                            addTimeSlot(timeSlots, time, config, pricePerMinDuration);
                        }
                    } else {
                        addTimeSlot(timeSlots, time, config, pricePerMinDuration);
                    }
                    time = time.plusMinutes(config.getMinInterval());
                }
                //make sure to display the last min interval of the day
                if (config.getMinInterval() < config.getMinDuration() && i == calendarConfigs.size()) {
                    if (time.plusMinutes(config.getMinInterval()).compareTo(endDateTime) <= 0) {
                        addTimeSlot(timeSlots, time, config, null);
                    }
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
        if (!overlaps(timeSlot, timeSlots)) {
            timeSlots.add(timeSlot);
        }
    }

    private boolean overlaps(TimeSlot timeSlot, List<TimeSlot> timeSlots) {
        for (TimeSlot slot : timeSlots) {
            if (timeSlot.getStartTime().equals(slot.getStartTime())) {
                return configMatches(timeSlot, slot);
            } else if (timeSlot.getStartTime().isBefore(slot.getStartTime())) {
                //make sure timeSlot ends before slot starts
                if (timeSlot.getEndTime().isAfter(slot.getStartTime())) {
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

    private boolean configMatches(TimeSlot timeSlot, TimeSlot slot) {
        return timeSlot.getConfig().equals(slot.getConfig());
    }

    public void checkForBookedCourts(TimeSlot timeSlot, List<Booking> confirmedBookings, Boolean preventOverlapping) {
        LocalTime startTime = timeSlot.getStartTime();
        LocalTime endTime = timeSlot.getEndTime();

        for (Booking booking : confirmedBookings) {

            if (timeSlot.getDate().equals(booking.getBookingDate())) {
                LocalTime bookingStartTime = booking.getBookingTime();
                LocalTime bookingEndTime = bookingStartTime.plusMinutes(booking.getDuration().intValue());
                Boolean addBooking = false;
                if (preventOverlapping) {
                    if (startTime.isBefore(bookingEndTime)) {
                        if (endTime.isAfter(bookingStartTime)) {
                            addBooking = true;
                        }
                    }
                } else {
                    //for displaying allocations
                    if (startTime.compareTo(bookingStartTime) >= 0) {
                        if (endTime.compareTo(bookingEndTime) <= 0) {
                            addBooking = true;
                        }
                    }
                }
                if (addBooking) {
                    Offer offer = booking.getOffer();
                    for (Offer timeSlotOffer : timeSlot.getConfig().getOffers()) {
                        if (offer.equals(timeSlotOffer)) {
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

    public void addWeekView(HttpServletRequest request, LocalDate selectedDate, List<Facility> selectedFacilities, ModelAndView mav, Boolean onlyFutureTimeSlots, Boolean preventOverlapping) throws JsonProcessingException {
        //calculate date configuration for datepicker
        LocalDate today = new LocalDate(DEFAULT_TIMEZONE);
        LocalDate firstDay = today.dayOfMonth().withMinimumValue();
        LocalDate lastDay = getLastBookableDay(request).plusDays(1);
        List<CalendarConfig> calendarConfigs = calendarConfigDAO.findBetween(firstDay, lastDay);
        Collections.sort(calendarConfigs);
        Map<String, DatePickerDayConfig> dayConfigs = getDayConfigMap(firstDay, lastDay, calendarConfigs);

        List<Booking> confirmedBookings = bookingDAO.findBlockedBookingsBetween(firstDay, lastDay);

        //calculate available time slots
        List<TimeSlot> timeSlots = new ArrayList<>();
        List<LocalDate> weekDays = new ArrayList<>();
        for (int i = 1; i <= CalendarWeekDay.values().length; i++) {
            LocalDate date = selectedDate.withDayOfWeek(i);
            weekDays.add(date);
            if ((!onlyFutureTimeSlots || !date.isBefore(today)) && lastDay.isAfter(date)) {
                try {
                    //generate list of bookable time slots
                    timeSlots.addAll(getTimeSlotsForDate(date, calendarConfigs, confirmedBookings, onlyFutureTimeSlots, preventOverlapping));
                } catch (CalendarConfigException e) {
                    //safe to ignore
                }
            }
        }

        SortedSet<Offer> offers = new TreeSet<>();
        List<TimeRange> rangeList = new ArrayList<>();
        //Map<TimeRange, List<TimeSlot>> rangeList = new TreeMap<>();
        for (TimeSlot slot : timeSlots) {
            Set<Offer> slotOffers = slot.getConfig().getOffers();
            offers.addAll(slotOffers);

            TimeRange range = new TimeRange();
            range.setStartTime(slot.getStartTime());
            range.setEndTime(slot.getEndTime());

            if (rangeList.contains(range)) {
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
        if (selectedFacilities.isEmpty()) {
            selectedOffers = offerDAO.findAll();
        } else {
            for (Facility facility : selectedFacilities) {
                selectedOffers.addAll(facility.getOffers());
            }
        }
        Collections.sort(selectedOffers);

        mav.addObject("dayConfigs", objectMapper.writeValueAsString(dayConfigs));
        mav.addObject("maxDate", lastDay.toString());
        mav.addObject("Day", selectedDate);
        mav.addObject("NextMonday", selectedDate.plusDays(8 - selectedDate.getDayOfWeek()));
        mav.addObject("PrevSunday", selectedDate.minusDays(selectedDate.getDayOfWeek()));
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
        for (Booking existingBooking : timeSlot.getBookings()) {
            if (existingBooking.getOffer().equals(offer)) {
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

    public void sendBookingConfirmation(HttpServletRequest request, Booking booking) throws MailException, IOException {
        Mail mail = new Mail();
        mail.setSubject(msg.get("BookingSuccessfulMailSubject"));
        mail.setTemplateId("HTMLEmail");
        String htmlBodyTemplate = msg.get("BookingSuccessfulMailBodyHtml");
        BookingMailSettings bookingMailSettings = bookingMailSettingsDAO.findFirst();
        if (bookingMailSettings != null) {
            if (!StringUtils.isEmpty(bookingMailSettings.getHtmlBodyTemplate())) {
                htmlBodyTemplate = bookingMailSettings.getHtmlBodyTemplate();
            }
        }
        // replace variables
        htmlBodyTemplate = htmlBodyTemplate.replace(BookingMailVariables.BOOKING_PLAYER.name(), booking.getPlayer().toString());
        htmlBodyTemplate = htmlBodyTemplate.replace(BookingMailVariables.BOOKING_DATE.name(), FormatUtils.DATE_MEDIUM.print(booking.getBookingDate()));
        htmlBodyTemplate = htmlBodyTemplate.replace(BookingMailVariables.BOOKING_TIME.name(), FormatUtils.TIME_HUMAN_READABLE.print(booking.getBookingTime()));
        htmlBodyTemplate = htmlBodyTemplate.replace(BookingMailVariables.BOOKING_OFFER.name(), booking.getName());
        htmlBodyTemplate = htmlBodyTemplate.replace(BookingMailVariables.BOOKING_PAYMENT_METHOD.name(), msg.get(booking.getPaymentMethod().toString()));
        htmlBodyTemplate = htmlBodyTemplate.replace(BookingMailVariables.BOOKING_AMOUNT.name(), booking.getPaymentMethod() == PaymentMethod.ExternalVoucher ? "" : booking.getAmount().toString());
        htmlBodyTemplate = htmlBodyTemplate.replace(BookingMailVariables.BOOKING_CURRENCY.name(), booking.getCurrency().getSymbol());
        htmlBodyTemplate = htmlBodyTemplate.replace(BookingMailVariables.BOOKING_CANCELLATION_POLICY_DEADLINE.name(), CANCELLATION_POLICY_DEADLINE.toString());
        htmlBodyTemplate = htmlBodyTemplate.replace(BookingMailVariables.BOOKING_CANCELLATION_URL.name(), RequestUtil.getBaseURL(request) + "/bookings/booking/" + booking.getUUID() + "/cancel");
        htmlBodyTemplate = htmlBodyTemplate.replace(BookingMailVariables.HOMEPAGE_URL.name(), RequestUtil.getBaseURL(request));

        mail.setHtmlBody(htmlBodyTemplate);

        mail.addRecipient(booking.getPlayer());
        mail.setAttachments(getAttachments(booking));
        mailUtils.send(mail, request);
    }

    public void sendEventBookingConfirmation(HttpServletRequest request, Booking booking) throws MailException, IOException {
        Mail mail = new Mail();
        mail.setSubject(msg.get("EventBookingSuccessfulMailSubject"));
        mail.setBody(msg.get("BookingEventSuccessfulMailBody", new Object[]{
                booking.getPlayer().toString(),
                FormatUtils.DATE_MEDIUM.print(booking.getBookingDate()),
                FormatUtils.TIME_HUMAN_READABLE.print(booking.getBookingTime()),
                booking.getName(),
                msg.get(booking.getPaymentMethod().toString()),
                booking.getPaymentMethod() == PaymentMethod.ExternalVoucher ? "" : booking.getAmount(),
                booking.getCurrency(),
                (StringUtils.isEmpty(booking.getEvent().getConfirmationMailRemark()) ? msg.get("None") : booking.getEvent().getConfirmationMailRemark()),
                RequestUtil.getBaseURL(request)}));
        mail.addRecipient(booking.getPlayer());
        mail.setAttachments(getAttachments(booking));
        mailUtils.send(mail, request);
    }

    public void sendNewBookingNotification(HttpServletRequest request, Booking booking) throws MailException, IOException {
        Set<Contact> contactsToNotifyOnBooking = Sets.newHashSet(contactDAO.findAllForBookings());
        if (!contactsToNotifyOnBooking.isEmpty()) {
            Mail mail = new Mail();
            mail.setSubject(msg.get("BookingSuccessfulMailSubjectAdmin", new Object[]{
                    FormatUtils.DATE_HUMAN_READABLE.print(booking.getBookingDate()),
                    FormatUtils.TIME_HUMAN_READABLE.print(booking.getBookingTime()),
                    booking.getPlayer().toString()
            }));
            mail.setBody(msg.get("BookingSuccessfulMailBodyAdmin", getDetailBody(request, booking)));
            mail.setRecipients(contactsToNotifyOnBooking);
            mailUtils.send(mail, request);
        }
    }

    public void sendBookingCancellationNotification(HttpServletRequest request, Booking booking) throws MailException, IOException {
        List<Contact> contactsToNotifyOnBookingCancellation = contactDAO.findAllForBookingCancellations();
        if (!contactsToNotifyOnBookingCancellation.isEmpty()) {
            Mail mail = new Mail();
            mail.setSubject(msg.get("BookingCancelledAdminMailSubject", new Object[]{
                    FormatUtils.DATE_HUMAN_READABLE.print(booking.getBookingDate()),
                    FormatUtils.TIME_HUMAN_READABLE.print(booking.getBookingTime()),
                    booking.getPlayer().toString()
            }));
            mail.setBody(msg.get("BookingCancelledAdminMailBody", getDetailBody(request, booking)));
            mail.addRecipient(booking.getPlayer());
            mailUtils.send(mail, request);
        }
    }

    public LocalDate getLastBookableDay(HttpServletRequest request) {
        Integer maxDate = Constants.CALENDAR_MAX_DATE;
        if (sessionUtil.getPrivileges(request).contains(Privilege.ManageBookings)) {
            maxDate = Constants.CALENDAR_MAX_DATE_ADMINS;
        }
        return new LocalDate(DEFAULT_TIMEZONE).plusDays(maxDate);
    }

    private Object[] getDetailBody(HttpServletRequest request, Booking booking) {
        return new Object[]{
                booking.getPlayer().toString(),
                FormatUtils.DATE_HUMAN_READABLE.print(booking.getBookingDate()),
                FormatUtils.TIME_HUMAN_READABLE.print(booking.getBookingTime()),
                booking.getName(),
                msg.get(booking.getPaymentMethod().toString()),
                booking.getAmount(),
                booking.getCurrency(),
                RequestUtil.getBaseURL(request) + "/invoices/booking/" + booking.getUUID(),
                RequestUtil.getBaseURL(request) + "/admin/reports/booking/" + booking.getId()
        };
    }

    public OfferDurationPrice getOfferDurationPrice(List<CalendarConfig> configs, List<Booking> confirmedBookings, LocalDate selectedDate, LocalTime selectedTime, Offer selectedOffer) throws CalendarConfigException {
        List<TimeSlot> timeSlotsForDate = getTimeSlotsForDate(selectedDate, configs, confirmedBookings, Boolean.TRUE, Boolean.TRUE);
        boolean validStartTime = false;
        for (TimeSlot timeSlot : timeSlotsForDate) {
            if (timeSlot.getStartTime().equals(selectedTime)) {
                validStartTime = true;
                break;
            }
        }

        OfferDurationPrice offerDurationPrices = null;
        if (validStartTime) {
            //convert to required data structure
            Map<Offer, List<CalendarConfig>> offerConfigMap = new HashMap<>();
            for (CalendarConfig config : configs) {
                for (Offer offer : config.getOffers()) {
                    if (offer.equals(selectedOffer)) {
                        List<CalendarConfig> list = offerConfigMap.get(offer);
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        list.add(config);

                        //sort by start time
                        Collections.sort(list);
                        offerConfigMap.put(offer, list);
                    }
                }
            }


            Iterator<Map.Entry<Offer, List<CalendarConfig>>> iterator = offerConfigMap.entrySet().iterator();
            //for every offer
            while (iterator.hasNext()) {
                Map.Entry<Offer, List<CalendarConfig>> entry = iterator.next();
                Offer offer = entry.getKey();
                List<CalendarConfig> configsForOffer = entry.getValue();

                //make sure the first configuration starts before the requested booking time
                if (selectedTime.compareTo(configsForOffer.get(0).getStartTime()) < 0) {
                    continue;
                }

                LocalDateTime endTime = null;
                Integer duration = configsForOffer.get(0).getMinDuration();
                BigDecimal pricePerMinute;
                BigDecimal price = null;
                CalendarConfig previousConfig = null;
                Map<Integer, BigDecimal> durationPriceMap = new TreeMap<>();
                Boolean isContiguous = true;
                for (CalendarConfig config : configsForOffer) {

                    //make sure there is no gap between calendar configurations
                    if (endTime == null) {
                        //first run
                        endTime = getLocalDateTime(selectedDate, selectedTime).plusMinutes(config.getMinDuration());
                    } else {
                        //break if there are durations available and calendar configs are not contiguous
                        if (!durationPriceMap.isEmpty()) {
                            //we substract min interval before the comparison as it has been added during the last iteration
                            LocalDateTime configStartDateTime = getLocalDateTime(selectedDate, config.getStartTime());
                            if (!endTime.minusMinutes(config.getMinInterval()).equals(configStartDateTime)) {
                                break;
                            }
                        }
                    }


                    Integer interval = config.getMinInterval();

                    pricePerMinute = getPricePerMinute(config);

                    //as long as the endTime is before the end time configured in the calendar
                    LocalDateTime configEndDateTime = getLocalDateTime(selectedDate, config.getEndTime());
                    while (endTime.compareTo(configEndDateTime) <= 0) {
                        TimeSlot timeSlot = new TimeSlot();
                        timeSlot.setDate(selectedDate);
                        timeSlot.setStartTime(selectedTime);
                        timeSlot.setEndTime(endTime.toLocalTime());
                        timeSlot.setConfig(config);
                        Long bookingSlotsLeft = getBookingSlotsLeft(timeSlot, offer, confirmedBookings);

                        //we only allow contiguous bookings for any given offer
                        if (bookingSlotsLeft < 1) {
                            isContiguous = false;
                            break;
                        }

                        if (price == null) {
                            //see if previousConfig endTime - minInterval matches the selected time. if so, take half of the previous config price as a basis
                            if (previousConfig != null && previousConfig.getEndTime().minusMinutes(previousConfig.getMinInterval()).equals(selectedTime)) {
                                BigDecimal previousConfigPricePerMinute = getPricePerMinute(previousConfig);
                                price = previousConfigPricePerMinute.multiply(new BigDecimal(previousConfig.getMinInterval()), MathContext.DECIMAL128);
                                price = price.add(pricePerMinute.multiply(new BigDecimal(duration - previousConfig.getMinInterval()), MathContext.DECIMAL128));
                            } else {
                                price = pricePerMinute.multiply(new BigDecimal(duration.toString()), MathContext.DECIMAL128);
                            }
                        } else {
                            //add price for additional interval
                            price = price.add(pricePerMinute.multiply(new BigDecimal(interval.toString()), MathContext.DECIMAL128));
                        }
                        price = price.setScale(2, RoundingMode.HALF_EVEN);
                        durationPriceMap.put(duration, price);

                        //increase the duration by the configured minimum interval and determine the new end time for the next iteration
                        duration += interval;
                        endTime = endTime.plusMinutes(interval);
                    }

                    if (!durationPriceMap.isEmpty()) {
                        OfferDurationPrice odp = new OfferDurationPrice();
                        odp.setOffer(offer);
                        odp.setDurationPriceMap(durationPriceMap);
                        odp.setConfig(config);
                        offerDurationPrices = odp;
                    }

                    if (!isContiguous) {
                        //we only allow coniguous bookings for one offer. process next offer
                        break;
                    }
                    previousConfig = config;

                }
            }
        }
        return offerDurationPrices;
    }

    public List<PaymentMethod> getActivePaymentMethods() {
        //determine valid payment methods
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        //always support subscriptions, cash and vouchers
        paymentMethods.add(PaymentMethod.Subscription);
        paymentMethods.add(PaymentMethod.Cash);
        paymentMethods.add(PaymentMethod.ExternalVoucher);
        paymentMethods.add(PaymentMethod.Voucher);

        //check if PayPal config exists and is active
        PayPalConfig paypalConfig = payPalConfigDAO.findFirst();
        if (paypalConfig != null && paypalConfig.getActive()) {
            paymentMethods.add(PaymentMethod.PayPal);
        }

        //check if PayDirekt config exists and is active
        PayDirektConfig payDirektConfig = payDirektConfigDAO.findFirst();
        if (payDirektConfig != null && payDirektConfig.getActive()) {
            paymentMethods.add(PaymentMethod.PayDirekt);
        }

        //check if PayMill config exists
        PayMillConfig payMillConfig = payMillConfigDAO.findFirst();
        if (payMillConfig != null) {
            if (payMillConfig.getEnableDirectDebit()) {
                paymentMethods.add(PaymentMethod.DirectDebit);
            }
            if (payMillConfig.getEnableCreditCard()) {
                paymentMethods.add(PaymentMethod.CreditCard);
            }
        }
        return paymentMethods;
    }

    private Set<Attachment> getAttachments(Booking booking) {
        Set<Attachment> attachments = new HashSet<>();
        Attachment attachment = getCalendarAttachment(booking);
        if (attachment != null) {
            attachments.add(attachment);
        }
        return attachments;
    }

    private Attachment getCalendarAttachment(Booking booking) {
        try {
            LocalDate startLocalDate = booking.getEvent() == null ? booking.getBookingDate() : booking.getEvent().getStartDate();
            //DateTime startUtcDate = startLocalDate.toDateTimeAtStartOfDay(Constants.DEFAULT_TIMEZONE);
            LocalTime startTime = booking.getEvent() == null ? booking.getBookingTime() : booking.getEvent().getStartTime();
            LocalTime endTime = booking.getEvent() == null ? booking.getBookingEndTime() : booking.getEvent().getStartTime().plusHours(2);
            String calendarEntryName = booking.getEvent() == null ? booking.getOffer().getName() : booking.getEvent().getName();

            String organizerName = booking.getPlayer().toString();
            String organizerEmail = booking.getPlayer().getEmail();
            String summary = String.format("%s - %s", calendarEntryName, booking.getCustomer().getName());
            String dtstamp = FormatUtils.DATE_TIME_MACHINE_READABLE.print(DateTime.now(Constants.DEFAULT_TIMEZONE));
            String dtstart = FormatUtils.DATE_MACHINE_READABLE.print(startLocalDate) + FormatUtils.TIME_MACHINE_READABLE.print(startTime);
            String dtend = FormatUtils.DATE_MACHINE_READABLE.print(startLocalDate) + FormatUtils.TIME_MACHINE_READABLE.print(endTime);
            String uid = booking.getUUID().toString();
            String calendarTemplate = FileUtil.getFileContents("calendar_template.ics");
            String data = String.format(calendarTemplate, organizerName, organizerName, organizerEmail, summary, summary, dtstamp, DEFAULT_TIMEZONE_STRING, dtstart, DEFAULT_TIMEZONE_STRING, dtend, uid);

            Attachment attachment = new Attachment();
            attachment.setType("text/calendar");
            attachment.setName(String.format("%s %s.ics", booking.getCustomer().getDomainName(), calendarEntryName.replace(" ", "_")));
            attachment.setData(Base64.encodeBase64String(data.getBytes(StandardCharsets.UTF_8)));
            return attachment;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

}
