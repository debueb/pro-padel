/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller;

import de.appsolve.padelcampus.constants.BookingType;
import de.appsolve.padelcampus.constants.CalendarWeekDay;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.constants.Currency;
import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.data.ReservationRequest;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.CalendarConfigDAOI;
import de.appsolve.padelcampus.db.dao.GenericDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import de.appsolve.padelcampus.utils.BookingUtil;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired
    BookingDAOI bookingDAO;
    
    @Autowired
    BookingUtil bookingUtil;
    
    @Autowired
    CalendarConfigDAOI calendarConfigDAO;
    
    @Autowired
    SessionUtil sessionUtil;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
    }
    
    @RequestMapping()
    @Override
    public ModelAndView showIndex(){
        ModelAndView mav = new ModelAndView("admin/bookings/reservations/index");
        List<Booking> reservations = bookingDAO.findReservations();
        mav.addObject("Reservations", reservations);
        return mav;
    }
    
    @RequestMapping("add")
    @Override
    public ModelAndView showAddView(){
        ReservationRequest request = new ReservationRequest();
        request.setStartTimeHour(Constants.BOOKING_DEFAULT_VALID_FROM_HOUR);
        request.setStartTimeMinute(Constants.BOOKING_DEFAULT_VALID_FROM_MINUTE);
        LocalTime endTime = request.getStartTime().plusMinutes(Constants.BOOKING_DEFAULT_DURATION);
        request.setEndTimeHour(endTime.getHourOfDay());
        request.setEndTimeMinute(endTime.getMinuteOfHour());
        request.setCourtCount(1);
        return getAddView(request);
    }
    
    @RequestMapping(value="add", method=POST)
    @Override
    public ModelAndView postEditView(@Valid @ModelAttribute("Model") ReservationRequest reservationRequest, HttpServletRequest request, BindingResult bindingResult){
        try {
            if (reservationRequest.getCalendarWeekDays().isEmpty()){
                throw new Exception(msg.get("SelectAtLeastOneWeekDay"));
            }
            if (reservationRequest.getCourtCount() == null){
                throw new Exception(msg.get("SpecifyCourtCount"));
            }
            
            Player player = sessionUtil.getUser(request);
            
            //calculate reservation bookings, taking into account holidays
            LocalDate date = reservationRequest.getStartDate();
            LocalDate endDate = reservationRequest.getEndDate();
            
            List<Booking> bookings = new ArrayList<>();
            Map<Booking, Exception> failedBookings = new TreeMap<>();
            
            while (date.compareTo(endDate) <= 0){
                Set<CalendarWeekDay> calendarWeekDays = reservationRequest.getCalendarWeekDays();
                for (CalendarWeekDay calendarWeekDay : calendarWeekDays) {
                    if (calendarWeekDay.ordinal()+1 == date.getDayOfWeek()){
                        List<CalendarConfig> calendarConfigsForDate = calendarConfigDAO.findFor(date);
                        Iterator<CalendarConfig> iterator = calendarConfigsForDate.iterator();
                        while(iterator.hasNext()){
                            CalendarConfig calendarConfig = iterator.next();
                            if (!bookingUtil.isHoliday(date, calendarConfig)) {
                                for (int i=0; i<reservationRequest.getCourtCount(); i++){
                                    Booking booking = new Booking();
                                    booking.setAmount(BigDecimal.ZERO);
                                    booking.setBlockingTime(new LocalDateTime());
                                    booking.setBookingDate(date);
                                    booking.setBookingTime(reservationRequest.getStartTime());
                                    booking.setBookingType(BookingType.reservation);
                                    booking.setConfirmed(true);
                                    booking.setCurrency(Currency.EUR);
                                    int minutes = Minutes.minutesBetween(reservationRequest.getStartTime(), reservationRequest.getEndTime()).getMinutes(); 
                                    if (minutes < calendarConfig.getMinDuration()){
                                        throw new Exception(msg.get("MinDurationIs", new Object[]{calendarConfig.getMinDuration()}));
                                    }
                                    booking.setDuration(new Long(minutes));
                                    booking.setPaymentConfirmed(true);
                                    booking.setPaymentMethod(PaymentMethod.Reservation);
                                    booking.setPlayer(player);
                                    booking.setUUID(BookingUtil.generateUUID());

                                    //throws exception if there are not enough free courts for desired booking
                                    try {
                                        Integer courtNumber = bookingUtil.getCourtNumber(booking);
                                        booking.setCourtNumber(courtNumber);

                                        //we save the booking directly so that the court number gets updated correctly
                                        bookingDAO.saveOrUpdate(booking);
                                        bookings.add(booking);
                                    } catch (Exception e){
                                        failedBookings.put(booking, e);
                                    }
                                }
                            }
                            break;
                        }
                        break;
                    }
                }
                date = date.plusDays(1);
            }
            
            if (!failedBookings.isEmpty()){
                StringBuilder sb = new StringBuilder();
                for (Entry<Booking, Exception> entry: failedBookings.entrySet()){
                    sb.append(entry.getKey().toString());
                    sb.append(": ");
                    sb.append(entry.getValue().getMessage());
                    sb.append("<br/>");
                }
                throw new Exception(msg.get("UnableToReserveAllDesiredTimes", new Object[]{StringUtils.join(bookings, "<br/>"), sb.toString()}));
            }
            
            if (bookings.isEmpty()){
                throw new Exception(msg.get("NoCourtReservationsForSelectedDateTime"));
            }
            
            return new ModelAndView("redirect:/admin/bookings/reservations");
        } catch (Exception e){
            ModelAndView mav = getAddView(reservationRequest);
            bindingResult.addError(new ObjectError("courtCount", e.getMessage()));
            return mav;
        }
    }

    private ModelAndView getAddView(ReservationRequest reservationRequest) {
        ModelAndView mav = new ModelAndView("admin/bookings/reservations/add");
        mav.addObject("Model", reservationRequest);
        mav.addObject("WeekDays", CalendarWeekDay.values());
        return mav;
    }

    @Override
    public GenericDAOI getDAO() {
        return bookingDAO;
    }

    @Override
    public String getModuleName() {
        return "bookings/reservations";
    }
}
