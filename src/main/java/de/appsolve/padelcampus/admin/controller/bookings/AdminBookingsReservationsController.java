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
import de.appsolve.padelcampus.data.OfferDurationPrice;
import de.appsolve.padelcampus.data.ReservationRequest;
import de.appsolve.padelcampus.data.TimeSlot;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.CalendarConfigDAOI;
import de.appsolve.padelcampus.db.dao.MasterDataDAOI;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.dao.OfferDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.db.model.MasterData;
import de.appsolve.padelcampus.db.model.Offer;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.exceptions.CalendarConfigException;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import de.appsolve.padelcampus.spring.OfferOptionCollectionEditor;
import de.appsolve.padelcampus.utils.BookingUtil;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import jersey.repackaged.com.google.common.collect.Sets;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/bookings/reservations")
public class AdminBookingsReservationsController extends AdminBaseController<ReservationRequest>{
    
    private static final Logger LOG = Logger.getLogger(AdminBookingsReservationsController.class);
    
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
    
    @Autowired
    MasterDataDAOI masterDataDAO;
    
    @Autowired
    OfferOptionCollectionEditor offerOptionCollectionEditor;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Set.class, "offerOptions", offerOptionCollectionEditor);
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
        binder.registerCustomEditor(SortedSet.class, "offers", new CustomCollectionEditor(SortedSet.class) {
            @Override
            protected Object convertElement(Object element) {
                if (element instanceof Offer){
                    return element;
                }
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
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(startDate);
        return getIndexView(dateRange);
    }
    
    @RequestMapping(method=POST)
    public ModelAndView postIndex(HttpServletRequest request, @Valid @ModelAttribute("DateRange") DateRange dateRange){
        sessionUtil.setBookingListStartDate(request, dateRange.getStartDate());
        if (dateRange.getEndDate().isBefore(dateRange.getStartDate())){
            dateRange.setEndDate(dateRange.getStartDate());
        }
        return getIndexView(dateRange); 
    }
    
    @Override
    public ModelAndView showAddView(HttpServletRequest httpRequest){
        ReservationRequest request = new ReservationRequest();
        request.setPublicBooking(Boolean.TRUE);
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
            Set<Booking> failedBookings = new HashSet<>();
            LocalDateTime blockingTime = new LocalDateTime();
            
            while (date.compareTo(endDate) <= 0){
                Set<CalendarWeekDay> calendarWeekDays = reservationRequest.getCalendarWeekDays();
                for (CalendarWeekDay calendarWeekDay: calendarWeekDays) {
                    if (calendarWeekDay.ordinal()+1 == date.getDayOfWeek()){
                        try {
                            List<CalendarConfig> calendarConfigs = calendarConfigDAO.findFor(date); //throws CalendarConfigException
                            Iterator<CalendarConfig> iterator = calendarConfigs.iterator();
                            while(iterator.hasNext()){
                                CalendarConfig calendarConfig = iterator.next();
                                if (!bookingUtil.isHoliday(date, calendarConfig)) {
                                    for (Offer offer: reservationRequest.getOffers()){

                                        Booking booking = new Booking();
                                        booking.setAmount(BigDecimal.ZERO);
                                        booking.setBlockingTime(blockingTime);
                                        booking.setBookingDate(date);
                                        booking.setBookingTime(reservationRequest.getStartTime());
                                        booking.setBookingType(BookingType.reservation);
                                        booking.setComment(reservationRequest.getComment());
                                        booking.setConfirmed(Boolean.TRUE);
                                        booking.setCurrency(Currency.EUR);
                                        booking.setDuration(getDuration(reservationRequest, calendarConfig));
                                        booking.setPaymentConfirmed(reservationRequest.getPaymentConfirmed());
                                        booking.setPaymentMethod(PaymentMethod.Reservation);
                                        booking.setPlayer(player);
                                        booking.setUUID(BookingUtil.generateUUID());
                                        booking.setOffer(offer);
                                        booking.setPublicBooking(reservationRequest.getPublicBooking());
                                        
                                        //we call this inside the loop to prevent overbooking
                                        List<Booking> confirmedBookings = bookingDAO.findBlockedBookingsForDate(date);
                                        
                                        OfferDurationPrice offerDurationPrice = bookingUtil.getOfferDurationPrice(calendarConfigs, confirmedBookings, booking.getBookingDate(), booking.getBookingTime(), offer);
                                        if (offerDurationPrice == null){
                                            failedBookings.add(booking);
                                            continue;
                                        } else {
                                            BigDecimal price = offerDurationPrice.getDurationPriceMap().get(booking.getDuration().intValue());
                                            booking.setAmount(price);
                                        }

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
                                        booking = bookingDAO.saveOrUpdate(booking);
                                        bookings.add(booking);
                                    }
                                }
                                break;
                            }
                            break;
                        } catch (CalendarConfigException e){
                            LOG.warn("Caught calendar config exception during add reservation request. This may be normal (for holidays)", e);
                            Booking failedBooking = new Booking();
                            failedBooking.setPlayer(player);
                            failedBooking.setBookingDate(date);
                            failedBooking.setBookingTime(reservationRequest.getStartTime());
                            failedBookings.add(failedBooking);
                        }
                    }
                }
                date = date.plusDays(1);
            }
            
            if (!failedBookings.isEmpty()){
                throw new Exception(msg.get("UnableToReserveAllDesiredTimes", new Object[]{StringUtils.join(bookings, "<br/>"), StringUtils.join(failedBookings, "<br/>")}));
            }
            
            if (bookings.isEmpty()){
                throw new Exception(msg.get("NoCourtReservationsForSelectedDateTime"));
            }
            
            return new ModelAndView("redirect:/admin/bookings/reservations");
        } catch (Exception e){
            LOG.error(e, e);
            bindingResult.addError(new ObjectError("comment", e.getMessage()));
            return addView;
        }
    }
    
    @RequestMapping(method = GET, value="booking/{bookingId}")
    public ModelAndView getEditBooking(@PathVariable("bookingId") Long bookingId){
        Booking booking = bookingDAO.findByIdWithOfferOptions(bookingId);
        if (booking == null){
            return getNotFoundView();
        }
        ReservationRequest request = getReservationRequestFromBooking(booking);
        return getEditView(request);
    }
    
    @RequestMapping(method = POST, value="booking/{bookingId}")
    public ModelAndView postEditBooking(@PathVariable("bookingId") Long bookingId, @Valid @ModelAttribute("Model") ReservationRequest model, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return getEditView(model);
        }
        try {
            LocalDate today = new LocalDate();
            LocalTime now = new LocalTime();
            if (model.getStartDate().compareTo(today)<0 || (model.getStartDate().equals(today) && model.getStartTime().compareTo(now)<0)){
                throw new Exception(msg.get("RequestedTimeIsInThePast"));
            }

            /* only one offer possible */
            Offer offer = model.getOffers().iterator().next();
            Booking booking = bookingDAO.findById(bookingId);
            
            List<CalendarConfig> calendarConfigs = calendarConfigDAO.findFor(model.getStartDate()); //throws CalendarConfigException
            Iterator<CalendarConfig> iterator = calendarConfigs.iterator();
            while(iterator.hasNext()){
                CalendarConfig calendarConfig = iterator.next();
                if (!bookingUtil.isHoliday(model.getStartDate(), calendarConfig)) {
                    booking.setBlockingTime(new LocalDateTime());
                    booking.setBookingDate(model.getStartDate());
                    booking.setBookingTime(model.getStartTime());
                    booking.setComment(model.getComment());
                    booking.setDuration(getDuration(model, calendarConfig));
                    booking.setOffer(offer);
                    booking.setOfferOptions(model.getOfferOptions());
                    booking.setPaymentConfirmed(model.getPaymentConfirmed());
                    booking.setPaymentMethod(PaymentMethod.Reservation);
                    booking.setPublicBooking(model.getPublicBooking());
                    
                    List<Booking> confirmedBookings = bookingDAO.findBlockedBookingsForDate(booking.getBookingDate());
                    //remove the current booking as we want to update it
                    confirmedBookings.remove(booking);
            
                    OfferDurationPrice offerDurationPrice = bookingUtil.getOfferDurationPrice(calendarConfigs, confirmedBookings, booking.getBookingDate(), booking.getBookingTime(), offer);
                    if (offerDurationPrice != null){
                        BigDecimal price = offerDurationPrice.getDurationPriceMap().get(booking.getDuration().intValue());
                        booking.setAmount(price);
                    
                        TimeSlot timeSlot = new TimeSlot();
                        timeSlot.setDate(booking.getBookingDate());
                        timeSlot.setStartTime(booking.getBookingTime());
                        timeSlot.setEndTime(booking.getBookingEndTime());
                        timeSlot.setConfig(calendarConfig);
                        Long bookingSlotsLeft = bookingUtil.getBookingSlotsLeft(timeSlot, offer, confirmedBookings);

                        if (bookingSlotsLeft >= 1){
                            bookingDAO.saveOrUpdate(booking);
                            return redirectToIndex();
                        }
                    }
                }
            }
            throw new Exception(msg.get("NoCourtReservationsForSelectedDateTime"));
        } catch (Exception e){
            LOG.error(e);
            bindingResult.addError(new ObjectError("id", e.getMessage()));
            return getEditView(model);
        }
    }
    
    @RequestMapping(method = GET, value="/{bookingId}/deleteall")
    public ModelAndView getBookingDeleteAll(@PathVariable("bookingId") Long bookingId){
        Booking booking = bookingDAO.findById(bookingId);
        if (booking == null){
            return getNotFoundView();
        }
        List<Booking> commentBookings = bookingDAO.findByBlockingTimeAndComment(booking.getBlockingTime(), booking.getComment());
        return getBookingDeleteAllView(booking, commentBookings);
    }
    
    @RequestMapping(method = POST, value="/{bookingId}/deleteall")
    public ModelAndView postBookingDeleteAll(HttpServletRequest request, @PathVariable("bookingId") Long bookingId){
        Booking booking = bookingDAO.findById(bookingId);
        if (booking == null){
            return getNotFoundView();
        }
        List<Booking> commentBookings = bookingDAO.findByBlockingTimeAndComment(booking.getBlockingTime(), booking.getComment());
        bookingDAO.delete(commentBookings);
        return redirectToIndex(request);
    }
    
    @RequestMapping("print/{start}/{end}")
    public ModelAndView getPrintInvoices(@PathVariable("start") String start, @PathVariable("end") String end){
        MasterData masterData = masterDataDAO.findFirst();
        if (masterData == null){
            return new ModelAndView("invoices/masterdata_missing");
        }
        LocalDate startDate = new LocalDate(start);
        LocalDate endDate = new LocalDate(end);
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(startDate);
        dateRange.setEndDate(endDate);
        List<Booking> bookings = bookingDAO.findActiveBookingsBetween(dateRange.getStartDate(), dateRange.getEndDate());
        ModelAndView mav = new ModelAndView("admin/bookings/reservations/printinvoices");
        mav.addObject("MasterData", masterData);
        mav.addObject("Bookings", bookings);
        return mav;
    }
    
    private ModelAndView getIndexView(DateRange dateRange) {
        List<Booking> bookings = bookingDAO.findActiveBookingsBetween(dateRange.getStartDate(), dateRange.getStartDate());
        
        BigDecimal total = new BigDecimal(0);
        for (Booking booking: bookings){
            if (booking.getAmount() != null){
                total = total.add(booking.getAmount());
            }
        }
        ModelAndView listView = new ModelAndView("admin/bookings/reservations/index");
        listView.addObject("Total", total);
        listView.addObject("Bookings", bookings);
        listView.addObject("DateRange", dateRange);
        return listView;
    }
    
    @Override
    protected ModelAndView getEditView(ReservationRequest request) {
        ModelAndView mav = new ModelAndView("admin/bookings/reservations/edit");
        mav.addObject("Model", request);
        mav.addObject("Offers", offerDAO.findAllFetchWithOfferOptions());
        return mav;
    }
    
    private ModelAndView redirectToIndex() {
        return new ModelAndView("redirect:/admin/bookings/reservations");
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
    
    private ModelAndView getBookingDeleteAllView(Booking booking, List<Booking> bookingsToDelete) {
        ModelAndView mav = new ModelAndView("admin/bookings/reservations/deleteall");
        mav.addObject("Model", booking);
        mav.addObject("BookingsToDelete", bookingsToDelete);
        return mav;
    }

    private ReservationRequest getReservationRequestFromBooking(Booking booking) {
        ReservationRequest request = new ReservationRequest();
        request.setCalendarWeekDays(Sets.newHashSet(CalendarWeekDay.values()[booking.getBookingDate().dayOfWeek().get()-1]));
        request.setComment(booking.getComment());
        request.setStartDate(booking.getBookingDate());
        request.setStartTimeHour(booking.getBookingTime().getHourOfDay());
        request.setStartTimeMinute(booking.getBookingTime().getMinuteOfHour());
        request.setEndDate(booking.getBookingDate());
        LocalTime endTime = booking.getBookingTime().plusMinutes(booking.getDuration().intValue());
        request.setEndTimeHour(endTime.getHourOfDay());
        request.setEndTimeMinute(endTime.getMinuteOfHour());
        request.setId(booking.getId());
        request.setPublicBooking(booking.getPublicBooking());
        request.setPaymentConfirmed(booking.getPaymentConfirmed());
        request.setOffers(Sets.newHashSet(booking.getOffer()));
        request.setOfferOptions(booking.getOfferOptions());
        return request;
    }

    private Long getDuration(ReservationRequest reservationRequest, CalendarConfig calendarConfig) throws Exception {
        int minutes = Minutes.minutesBetween(reservationRequest.getStartTime(), reservationRequest.getEndTime()).getMinutes(); 
        if (minutes < calendarConfig.getMinDuration()){
            throw new Exception(msg.get("MinDurationIs", new Object[]{calendarConfig.getMinDuration()}));
        }
        return Long.valueOf(minutes);
    }

    @Override
    public ModelAndView getNotFoundView() {
        return new ModelAndView("admin/bookings/reservations/notfound");
    }
}
