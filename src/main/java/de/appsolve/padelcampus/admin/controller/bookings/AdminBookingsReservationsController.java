/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.bookings;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.constants.BookingType;
import de.appsolve.padelcampus.constants.CalendarWeekDay;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.constants.Currency;
import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.data.DateRange;
import de.appsolve.padelcampus.data.ReservationRequest;
import de.appsolve.padelcampus.data.TimeSlot;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.CalendarConfigDAOI;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.dao.OfferDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.db.model.Offer;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.exceptions.CalendarConfigException;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import de.appsolve.padelcampus.utils.BookingUtil;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/bookings/reservations")
public class AdminBookingsReservationsController extends AdminBaseController<ReservationRequest>{
    
    private static final Logger log = Logger.getLogger(AdminBookingsReservationsController.class);
    
    @Autowired
    BookingDAOI bookingDAO;
    
    @Autowired
    OfferDAOI offerDAO;
    
    @Autowired
    BookingUtil bookingUtil;
    
    @Autowired
    CalendarConfigDAOI calendarConfigDAO;
    
    @Autowired
    SessionUtil sessionUtil;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
        binder.registerCustomEditor(SortedSet.class, "offers", new CustomCollectionEditor(SortedSet.class) {
            @Override
            protected Object convertElement(Object element) {
                Long id = Long.parseLong((String) element);
                return offerDAO.findById(id);
            }
        });
    }
    
    @Override
    public ModelAndView showIndex(HttpServletRequest request, Pageable pageable, String search){
        LocalDate startDate = sessionUtil.getBookingListStartDate(request);
        if (startDate == null){
            startDate = new LocalDate();
        }
        LocalDate endDate = sessionUtil.getBookingListEndDate(request);
        if (endDate == null){
            endDate = startDate.plusMonths(4);
        }
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(startDate);
        dateRange.setEndDate(endDate);
        return getIndexView(dateRange);
    }
    
    @RequestMapping(method=POST)
    public ModelAndView postIndex(HttpServletRequest request, @Valid @ModelAttribute("DateRange") DateRange dateRange){
        sessionUtil.setBookingListStartDate(request, dateRange.getStartDate());
        sessionUtil.setBookingListEndDate(request, dateRange.getEndDate());
        return getIndexView(dateRange); 
    }
    
    @Override
    public ModelAndView showAddView(){
        ReservationRequest request = new ReservationRequest();
        request.setStartTimeHour(Constants.BOOKING_DEFAULT_VALID_FROM_HOUR);
        request.setStartTimeMinute(Constants.BOOKING_DEFAULT_VALID_FROM_MINUTE);
        LocalTime endTime = request.getStartTime().plusMinutes(Constants.BOOKING_DEFAULT_DURATION);
        request.setEndTimeHour(endTime.getHourOfDay());
        request.setEndTimeMinute(endTime.getMinuteOfHour());
        return getAddView(request);
    }
    
    @Override
    public ModelAndView postEditView(@ModelAttribute("Model") ReservationRequest reservationRequest, HttpServletRequest request, BindingResult bindingResult){
        ModelAndView addView = getAddView(reservationRequest);
        validator.validate(reservationRequest, bindingResult);
        if (bindingResult.hasErrors()){
            return addView;
        }
        try {
            Player player = sessionUtil.getUser(request);
            
            //calculate reservation bookings, taking into account holidays
            LocalDate date = reservationRequest.getStartDate();
            LocalDate endDate = reservationRequest.getEndDate();
            
            List<Booking> bookings = new ArrayList<>();
            Set<Booking> failedBookings = new TreeSet<>();
            
            while (date.compareTo(endDate) <= 0){
                Set<CalendarWeekDay> calendarWeekDays = reservationRequest.getCalendarWeekDays();
                for (CalendarWeekDay calendarWeekDay: calendarWeekDays) {
                    if (calendarWeekDay.ordinal()+1 == date.getDayOfWeek()){
                        try {
                            List<CalendarConfig> calendarConfigsForDate = calendarConfigDAO.findFor(date);
                            Iterator<CalendarConfig> iterator = calendarConfigsForDate.iterator();
                            while(iterator.hasNext()){
                                CalendarConfig calendarConfig = iterator.next();
                                if (!bookingUtil.isHoliday(date, calendarConfig)) {
                                    for (Offer offer: reservationRequest.getOffers()){

                                        Booking booking = new Booking();
                                        booking.setAmount(BigDecimal.ZERO);
                                        booking.setBlockingTime(new LocalDateTime());
                                        booking.setBookingDate(date);
                                        booking.setBookingTime(reservationRequest.getStartTime());
                                        booking.setBookingType(BookingType.reservation);
                                        booking.setComment(reservationRequest.getComment());
                                        booking.setConfirmed(reservationRequest.getPaymentConfirmed());
                                        booking.setCurrency(Currency.EUR);
                                        int minutes = Minutes.minutesBetween(reservationRequest.getStartTime(), reservationRequest.getEndTime()).getMinutes(); 
                                        if (minutes < calendarConfig.getMinDuration()){
                                            throw new Exception(msg.get("MinDurationIs", new Object[]{calendarConfig.getMinDuration()}));
                                        }
                                        booking.setDuration(Long.valueOf(minutes));
                                        booking.setPaymentConfirmed(true);
                                        booking.setPaymentMethod(PaymentMethod.Reservation);
                                        booking.setPlayer(player);
                                        booking.setUUID(BookingUtil.generateUUID());
                                        booking.setOffer(offer);

                                        List<Booking> confirmedBookings = bookingDAO.findBlockedBookingsForDate(date);
                                        TimeSlot timeSlot = new TimeSlot();
                                        timeSlot.setDate(date);
                                        timeSlot.setStartTime(reservationRequest.getStartTime());
                                        timeSlot.setEndTime(reservationRequest.getEndTime());
                                        timeSlot.setConfig(calendarConfig);
                                        Long bookingSlotsLeft = bookingUtil.getBookingSlotsLeft(timeSlot, offer, confirmedBookings);

                                        if (bookingSlotsLeft<1){
                                            failedBookings.add(booking);
                                            continue;
                                        }

                                        //we save the booking directly to prevent overbookings
                                        bookingDAO.saveOrUpdate(booking);
                                        bookings.add(booking);
                                    }
                                }
                                break;
                            }
                            break;
                        } catch (CalendarConfigException e){
                            log.warn("Caught calendar config exception during add reservation request. This may be normal (for holidays)", e);
                        }
                    }
                }
                date = date.plusDays(1);
            }
            
            if (!failedBookings.isEmpty()){
                StringBuilder sb = new StringBuilder();
                for (Booking booking: failedBookings){
                    sb.append(booking);
                    sb.append("<br/>");
                }
                throw new Exception(msg.get("UnableToReserveAllDesiredTimes", new Object[]{StringUtils.join(bookings, "<br/>"), sb.toString()}));
            }
            
            if (bookings.isEmpty()){
                throw new Exception(msg.get("NoCourtReservationsForSelectedDateTime"));
            }
            
            return new ModelAndView("redirect:/admin/bookings/reservations");
        } catch (Exception e){
            log.error(e);
            bindingResult.addError(new ObjectError("comment", e.toString()));
            return addView;
        }
    }

    private ModelAndView getAddView(ReservationRequest reservationRequest) {
        ModelAndView mav = new ModelAndView("admin/bookings/reservations/add");
        mav.addObject("Model", reservationRequest);
        mav.addObject("WeekDays", CalendarWeekDay.values());
        mav.addObject("Offers", offerDAO.findAll());
        return mav;
    }

    @Override
    public BaseEntityDAOI getDAO() {
        return bookingDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/bookings/reservations";
    }

    private ModelAndView getIndexView(DateRange dateRange) {
        ModelAndView mav = new ModelAndView("admin/bookings/reservations/index");
        List<Booking> reservations = bookingDAO.findActiveReservationsBetween(dateRange.getStartDate(), dateRange.getEndDate());
        mav.addObject("Reservations", reservations);
        mav.addObject("DateRange", dateRange);
        return mav;
    }
}
