package de.appsolve.padelcampus.controller.bookings;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.core.JsonProcessingException;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import de.appsolve.padelcampus.constants.CalendarWeekDay;
import de.appsolve.padelcampus.constants.Constants;
import static de.appsolve.padelcampus.constants.Constants.CANCELLATION_POLICY_DEADLINE;
import static de.appsolve.padelcampus.constants.Constants.DEFAULT_TIMEZONE;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.data.OfferDurationPrice;
import de.appsolve.padelcampus.data.TimeSlot;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.CalendarConfigDAOI;
import de.appsolve.padelcampus.db.dao.FacilityDAOI;
import de.appsolve.padelcampus.db.dao.OfferDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.dao.VoucherDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.db.model.Contact;
import de.appsolve.padelcampus.db.model.Facility;
import de.appsolve.padelcampus.db.model.Offer;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Voucher;
import de.appsolve.padelcampus.exceptions.CalendarConfigException;
import de.appsolve.padelcampus.utils.BookingUtil;
import de.appsolve.padelcampus.utils.FormatUtils;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE;
import static de.appsolve.padelcampus.utils.FormatUtils.TIME_HUMAN_READABLE;
import de.appsolve.padelcampus.utils.MailUtils;
import de.appsolve.padelcampus.utils.Msg;
import de.appsolve.padelcampus.utils.RequestUtil;
import de.appsolve.padelcampus.utils.SessionUtil;
import de.appsolve.padelcampus.utils.VoucherUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/bookings")
public class BookingsController extends BaseController {

    private static final Logger log = Logger.getLogger(BookingsController.class);

    @Autowired
    SessionUtil sessionUtil;

    @Autowired
    FacilityDAOI facilityDAO;
    
    @Autowired
    PlayerDAOI playerDAO;

    @Autowired
    BookingDAOI bookingDAO;

    @Autowired
    VoucherDAOI voucherDAO;

    @Autowired
    CalendarConfigDAOI calendarConfigDAO;

    @Autowired
    OfferDAOI offerDAO;
    
    @Autowired
    BookingsPayPalController bookingsPayPalController;

    @Autowired
    BookingsPayMillController bookingsPayMillController;

    @Autowired
    BookingsVoucherController bookingsVoucherController;

    @Autowired
    BookingUtil bookingUtil;
    
    @Autowired
    Msg msg;

    @RequestMapping()
    public ModelAndView getToday(
            @RequestParam(value="date", required = false) String date,
            @RequestParam(value="time", required = false) String time,
            @RequestParam(value="facilities", required = false) List<Long> facilityIds
    ) throws JsonProcessingException {
        if (StringUtils.isEmpty(date)){
            date = DATE_HUMAN_READABLE.print(new DateTime());
        }
        List<Facility> offers;
        if (facilityIds == null){
            offers = facilityDAO.findAll();
        } else {
            offers = facilityDAO.findAll(facilityIds);
        } 
        return getIndexView(date, time, offers);
    }

//    @RequestMapping("{day}")
//    public ModelAndView getDay(@PathVariable("day") String day) throws JsonProcessingException {
//        return getIndexView(day, null);
//    }
//
//    @RequestMapping("{day}/{time}")
//    public ModelAndView getDayTime(@PathVariable("day") String day, @PathVariable("time") String time) throws JsonProcessingException {
//        return getIndexView(day, time);
//    }
    
    @RequestMapping("{day}/{time}/offer/{offerId}")
    public ModelAndView showBookingView(@PathVariable("day") String day, @PathVariable("time") String time, @PathVariable("offerId") Long offerId, HttpServletRequest request) throws ParseException, Exception {
        ModelAndView bookingView = getBookingView();
        try {
            Offer offer = offerDAO.findById(offerId);
            validateAndAddObjectsToView(bookingView, request, new Booking(), day, time, offer);
        } catch (Exception e){
            bookingView.addObject("error", e.getMessage());
        }
        return bookingView;
    }

    @RequestMapping(value = "{day}/{time}/offer/{offerId}", method = POST)
    public ModelAndView selectBooking(@PathVariable("day") String day, @PathVariable("time") String time, @PathVariable("offerId") Long offerId, @ModelAttribute("Booking") Booking booking, BindingResult bindingResult, HttpServletRequest request) throws Exception {
        ModelAndView bookingView = getBookingView();
        Offer offer = offerDAO.findById(offerId);
        booking.setOffer(offer);
        try {
            validateAndAddObjectsToView(bookingView, request, booking, day, time, offer);
        } catch (Exception e){
            bookingView.addObject("error", e.getMessage());
            return bookingView;
        }
        sessionUtil.setBooking(request, booking);

        String confirmURL = getConfirmURL(day, time);
        switch (booking.getBookingType()) {

            case loggedIn:
                return getRedirectToConfirmView(confirmURL);

            case login:
                sessionUtil.setLoginRedirectPath(request, confirmURL);
                return getRedirectToLoginView();

            case register:
                sessionUtil.setLoginRedirectPath(request, confirmURL);
                return getRedirectToRegisterView();

            case nologin:
                return getRedirectToNoLoginView();
        }

        return bookingView;
    }

    @RequestMapping(value = "/nologin")
    public ModelAndView showNoLoginView(HttpServletRequest request) {
        return getNoLoginView(new Player());
    }

    @RequestMapping(value = "/nologin", method = POST)
    public ModelAndView processNoLoginView(@Valid @ModelAttribute("Model") Player player, BindingResult result, HttpServletRequest request) {
        ModelAndView noLoginView = getNoLoginView(player);
        if (result.hasErrors()) {
            return noLoginView;
        }
        Booking booking = sessionUtil.getBooking(request);
        if (booking == null) {
            result.addError(new ObjectError("id", msg.get("SessionTimeout")));
            return noLoginView;
        }
        Player existingPlayer = playerDAO.findByEmail(player.getEmail());
        if (existingPlayer != null && !StringUtils.isEmpty(existingPlayer.getPasswordHash())) {
            result.addError(new ObjectError("id", msg.get("EmailAlreadyRegistered")));
            return noLoginView;
        }
        playerDAO.saveOrUpdate(player);
        booking.setPlayer(player);
        sessionUtil.setBooking(request, booking);
        String day = booking.getBookingDate().toString(FormatUtils.DATE_HUMAN_READABLE);
        String time = booking.getBookingTime().toString(FormatUtils.TIME_HUMAN_READABLE);
        return getRedirectToConfirmView(getConfirmURL(day, time));
    }

    @RequestMapping(value = "{day}/{time}/confirm")
    public ModelAndView showConfirmView(HttpServletRequest request) {
        Booking booking = sessionUtil.getBooking(request);
        ModelAndView confirmView = getBookingConfirmView(booking);
        if (booking == null) {
            confirmView.addObject("error", msg.get("SessionTimeout"));
            return confirmView;
        }

        //user may have logged in or registered in the meantime - in contrast, the nologin method sets the player directly
        Player user = sessionUtil.getUser(request);
        if (user != null) {
            booking.setPlayer(user);
        }
        if (booking.getPlayer() == null) {
            confirmView.addObject("error", msg.get("SessionTimeout"));
            return confirmView;
        }

        //TODO: make sure all required info is available
        return confirmView;
    }

    @RequestMapping(value = "{day}/{time}/confirm", method = POST)
    public ModelAndView confirmBooking(HttpServletRequest request, @PathVariable("day") String day, @PathVariable("time") String time) throws Exception {
        Booking booking = sessionUtil.getBooking(request);
        ModelAndView confirmView = getBookingConfirmView(booking);
        try {
            if (booking == null) {
                throw new Exception(msg.get("SessionTimeout"));
            }
            String cancellationPolicyCheckbox = request.getParameter("accept-cancellation-policy");
            if (StringUtils.isEmpty(cancellationPolicyCheckbox) || !cancellationPolicyCheckbox.equals("on")) {
                throw new Exception(msg.get("BookingCancellationPolicyNotAccepted"));
            }

            //rerun checks (date time valid, overbooked...)
            validateAndAddObjectsToView(null, request, booking, day, time, booking.getOffer());

            if (booking.getConfirmed()) {
                throw new Exception(msg.get("BookingAlreadyConfirmed"));
            }

            booking.setBlockingTime(new LocalDateTime());
            booking.setUUID(BookingUtil.generateUUID());
            bookingDAO.saveOrUpdate(booking);

            switch (booking.getPaymentMethod()) {
                case PayPal:
                    return bookingsPayPalController.redirectToPaypal(booking, request);
                case DirectDebit:
                    return bookingsPayMillController.redirectToDirectDebit(booking);
                case CreditCard:
                    return bookingsPayMillController.redirectToCreditCard(booking);
                case Voucher:
                    return bookingsVoucherController.redirectToVoucher(booking);
                default:
                    confirmView.addObject("error", booking.getPaymentMethod() + " not implemented");
                    return confirmView;
            }
        } catch (Exception e) {
            log.error("Error while processing booking request: "+e.getMessage(), e);
            confirmView.addObject("error", e.getMessage());
            return confirmView;
        }
    }

    @RequestMapping(value = "booking/{UUID}/success")
    public ModelAndView showSuccessView(@PathVariable("UUID") String UUID, HttpServletRequest request) {
        ModelAndView mav = getBookingSuccessView();
        Booking booking = bookingDAO.findByUUID(UUID);
        try {
            if (!booking.getPaymentConfirmed()){
                throw new Exception(msg.get("PaymentHasNotBeenConfirmed"));
            }

            if (booking.getConfirmed()) {
                throw new Exception(msg.get("BookingAlreadyConfirmed"));
            }
            booking.setConfirmed(true);
            bookingDAO.saveOrUpdate(booking);

            Mail mail = new Mail(request);
            mail.setSubject(msg.get("BookingSuccessfulMailSubject"));
            mail.setBody(msg.get("BookingSuccessfulMailBody", new Object[]{
                booking.getPlayer().toString(),
                FormatUtils.DATE_MEDIUM.print(booking.getBookingDate()),
                FormatUtils.TIME_HUMAN_READABLE.print(booking.getBookingTime()),
                booking.getDuration() + " " + msg.get("Minutes"),
                booking.getOffer().toString(),
                msg.get(booking.getPaymentMethod().toString()),
                booking.getAmount(),
                booking.getCurrency(),
                CANCELLATION_POLICY_DEADLINE,
                RequestUtil.getBaseURL(request) + "/bookings/booking/" + booking.getUUID() + "/cancel",
                RequestUtil.getBaseURL(request)}));
            Contact contact = new Contact();
            contact.setEmailAddress(booking.getPlayer().getEmail());
            contact.setEmailDisplayName(booking.getPlayer().toString());
            mail.addRecipient(contact);
            MailUtils.send(mail);
            booking.setConfirmationMailSent(true);
            bookingDAO.saveOrUpdate(booking);
        } catch (MandrillApiError | IOException ex) {
            log.error("Error while sending booking confirmation email", ex);
            mav.addObject("error", msg.get("FailedToSendBookingConfirmationEmail", new Object[]{FormatUtils.DATE_MEDIUM.print(booking.getBookingDate()), FormatUtils.TIME_HUMAN_READABLE.print(booking.getBookingTime())}));
        } catch (Exception e){
            mav.addObject("error", e.getMessage());
        }
        return mav;
    }

    @RequestMapping(value = "booking/{UUID}/cancel")
    public ModelAndView showCancellationView(@PathVariable("UUID") String UUID, HttpServletRequest request) {
        Booking booking = bookingDAO.findByUUID(UUID);
        ModelAndView cancellationView = getCancellationView(booking);
        try {
            validateBookingCancellation(booking);
        } catch (Exception e) {
            cancellationView.addObject("error", e.getMessage());
        }
        return cancellationView;
    }
    
    @RequestMapping(value = "booking/{UUID}/abort")
    public ModelAndView abortBooking(@PathVariable("UUID") String UUID, HttpServletRequest request) {
        Booking booking = bookingDAO.findByUUID(UUID);
        booking.setBlockingTime(null);
        booking.setCancelled(Boolean.TRUE);
        booking.setCancelReason("User cancelled during payment process");
        bookingDAO.saveOrUpdate(booking);
        String redirectURL = request.getParameter("redirect");
        if (!StringUtils.isEmpty(redirectURL)){
            return new ModelAndView("redirect:"+redirectURL);
        }
        return new ModelAndView("redirect:/bookings");
    }

    @RequestMapping(value = "booking/{UUID}/cancel", method = POST)
    public ModelAndView cancelBooking(@PathVariable("UUID") String UUID, HttpServletRequest request) {
        Booking booking = bookingDAO.findByUUID(UUID);
        try {
            validateBookingCancellation(booking);
            Long maxDuration;
            LocalDate validUntilDate;
            LocalTime validFromTime;
            LocalTime validUntilTime;
            Set<Offer> offers;
            Set<CalendarWeekDay> weekDays;
            Voucher oldVoucher = booking.getVoucher();
            if (oldVoucher!=null){
                maxDuration     = oldVoucher.getDuration();
                offers          = oldVoucher.getOffers();
                validUntilDate  = oldVoucher.getValidUntil();
                validFromTime   = oldVoucher.getValidFromTime();
                validUntilTime  = oldVoucher.getValidUntilTime();
                weekDays        = oldVoucher.getCalendarWeekDays();
            } else {
                maxDuration     = booking.getDuration();
                offers          = new HashSet<>(Arrays.asList(booking.getOffer()));
                validUntilDate  = booking.getBookingDate().plusYears(1);
                validFromTime   = new LocalTime().withHourOfDay(Constants.BOOKING_DEFAULT_VALID_FROM_HOUR).withMinuteOfHour(Constants.BOOKING_DEFAULT_VALID_FROM_MINUTE);
                validUntilTime  = new LocalTime().withHourOfDay(Constants.BOOKING_DEFAULT_VALID_UNTIL_HOUR).withMinuteOfHour(Constants.BOOKING_DEFAULT_VALID_UNTIL_MINUTE);
                weekDays        = CalendarWeekDay.valuesAsSet();
            }
            
            String comment              = "Replacement voucher for Booking ["+booking.toString()+"]";
            Voucher voucher = VoucherUtil.createNewVoucher(comment, maxDuration, validUntilDate, validFromTime, validUntilTime, weekDays, offers);
            voucherDAO.saveOrUpdate(voucher);

            Mail mail = new Mail(request);
            mail.setSubject(msg.get("VoucherMailSubject"));
            mail.setBody(msg.get("VoucherMailBody", new Object[]{
                booking.getPlayer().toString(),
                RequestUtil.getBaseURL(request),
                voucher.getDuration(),
                FormatUtils.DATE_MEDIUM.print(voucher.getValidUntil()),
                voucher.getUUID(),
                RequestUtil.getBaseURL(request)}));
            Contact contact = new Contact();
            contact.setEmailAddress(booking.getPlayer().getEmail());
            contact.setEmailDisplayName(booking.getPlayer().toString());
            mail.addRecipient(contact);
            try {
                MailUtils.send(mail);
                booking.setCancelled(true);
                booking.setCancelReason("cancellation with replacement voucher");
                bookingDAO.saveOrUpdate(booking);
            } catch (MandrillApiError | IOException ex) {
                log.error("Error while sending booking cancellation success email", ex);
                throw (ex);
            }
        } catch (Exception e) {
            log.error("Error during booking cancellation", e);
            ModelAndView cancellationView = getCancellationView(booking);
            cancellationView.addObject("error", e.getMessage());
            return cancellationView;
        }
        return getCancellationSuccessView(booking);
    }

    private ModelAndView getIndexView(String day, String time, List<Facility> facilities) throws JsonProcessingException {
        LocalDate selectedDate = DATE_HUMAN_READABLE.parseLocalDate(day);
        LocalTime selectedTime = null;
        if (!StringUtils.isEmpty(time)){
            selectedTime = TIME_HUMAN_READABLE.parseLocalTime(time);
        }
        ModelAndView indexView = new ModelAndView("bookings/index");
        bookingUtil.addWeekView(selectedDate, selectedTime, facilities, indexView, true);
        return indexView;
    }

    private ModelAndView getBookingView() throws Exception {
        return new ModelAndView("bookings/booking");
    }
    
    private void validateAndAddObjectsToView(ModelAndView mav, HttpServletRequest request, Booking booking, String day, String time, Offer offer) throws Exception{
        LocalDate selectedDate = FormatUtils.DATE_HUMAN_READABLE.parseLocalDate(day);
        LocalTime selectedTime = FormatUtils.TIME_HUMAN_READABLE.parseLocalTime(time);

        LocalDate today = new LocalDate();
        LocalTime now = new LocalTime();
        if (selectedDate.compareTo(today)<=0 && selectedTime.compareTo(now)<0){
            throw new Exception(msg.get("RequestedTimeIsInThePast"));
        }

        //create a list of possible booking durations taking into account calendar configurations and confirmed bookings
        Set<OfferDurationPrice> offerDurationPrices = getOfferDurationPrices(selectedDate, selectedTime);
        
        //notify the user in case there are no durations bookable
        if (offerDurationPrices.isEmpty()){
            throw new Exception(msg.get("NoFreeCourtsForSelectedTimeAndDate"));
        }
        
        //store user provided data in the session booking
        booking.setBookingDate(selectedDate);
        booking.setBookingTime(selectedTime);
        
        //set currency and price if offer and duration have been selected
        if (booking.getOffer()!=null && booking.getDuration()!=null){
            for (OfferDurationPrice offerDurationPrice: offerDurationPrices){
                if (offerDurationPrice.getOffer().equals(booking.getOffer())){
                    BigDecimal price = offerDurationPrice.getDurationPriceMap().get(booking.getDuration().intValue());
                    booking.setAmount(price);
                    booking.setCurrency(offerDurationPrice.getConfig().getCurrency());
                    break;
                }
            }
        }
        
        sessionUtil.setBooking(request, booking);

        if (mav!=null){
            mav.addObject("Booking", booking);
            mav.addObject("OfferDurationPrices", offerDurationPrices);
            mav.addObject("SelectedOffer", offer);
        }
    }

    private void validateBookingCancellation(Booking booking) throws Exception {
        if (booking == null) {
            throw new Exception(msg.get("InvalidBooking"));
        }
        if (booking.getCancelled()) {
            throw new Exception(msg.get("BookingAlreadyCancelled"));
        }
        LocalDateTime now = new LocalDateTime(DEFAULT_TIMEZONE);
        LocalDateTime bookingTime = new LocalDateTime()
                .withDate(booking.getBookingDate().getYear(), booking.getBookingDate().getMonthOfYear(), booking.getBookingDate().getDayOfMonth())
                .withTime(booking.getBookingTime().getHourOfDay(), booking.getBookingTime().getMinuteOfHour(), 0, 0);
        if (now.isAfter(bookingTime)) {
            throw new Exception(msg.get("BookingCancellationDeadlineMissed"));
        }

        Duration duration = new Duration(now.toDateTime(DateTimeZone.UTC), bookingTime.toDateTime(DateTimeZone.UTC));
        if (duration.getStandardHours() < CANCELLATION_POLICY_DEADLINE) {
            throw new Exception(msg.get("BookingCancellationDeadlineMissed"));
        }
    }

    private ModelAndView getRedirectToLoginView() {
        return new ModelAndView("redirect:/login");
    }

    private ModelAndView getRedirectToRegisterView() {
        return new ModelAndView("redirect:/login/register");
    }

    private ModelAndView getRedirectToNoLoginView() {
        return new ModelAndView("redirect:/bookings/nologin");
    }

    private ModelAndView getNoLoginView(Player player) {
        return new ModelAndView("bookings/nologin", "Model", player);
    }

    private ModelAndView getRedirectToConfirmView(String confirmURL) {
        return new ModelAndView("redirect:" + confirmURL);
    }

    private ModelAndView getBookingSuccessView() {
        return new ModelAndView("bookings/success");
    }

    private ModelAndView getCancellationView(Booking booking) {
        return new ModelAndView("bookings/cancel", "Booking", booking);
    }

    private ModelAndView getCancellationSuccessView(Booking booking) {
        return new ModelAndView("bookings/cancel-success", "Booking", booking);
    }

    private String getConfirmURL(String day, String time) {
        return "/bookings/" + day + "/" + time + "/confirm";
    }

    public static ModelAndView getBookingConfirmView(Booking booking) {
        ModelAndView mav = new ModelAndView("bookings/confirm");
        mav.addObject("Booking", booking);
        mav.addObject("CancellationPolicyDeadline", CANCELLATION_POLICY_DEADLINE);
        return mav;
    }

    public static ModelAndView getRedirectToSuccessView(Booking booking) {
        return new ModelAndView("redirect:/bookings/booking/" + booking.getUUID() + "/success");
    }

    private Set<OfferDurationPrice> getOfferDurationPrices(LocalDate selectedDate, LocalTime selectedTime) throws CalendarConfigException {
        List<CalendarConfig> configs = calendarConfigDAO.findFor(selectedDate);
        List<Booking> confirmedBookings = bookingDAO.findBlockedBookingsForDate(selectedDate);

        //convert to required data structure
        Map<Offer, List<CalendarConfig>> offerConfigMap = new HashMap<>();
        for (CalendarConfig config: configs){
            for (Offer offer: config.getOffers()){
                List<CalendarConfig> list = offerConfigMap.get(offer);
                if (list==null){
                    list = new ArrayList<>();
                }
                list.add(config);
                
                //sort by start time
                Collections.sort(list);
                offerConfigMap.put(offer, list);
            }
        }
        
        Set<OfferDurationPrice> offerDurationPrices = new TreeSet<>();
        
        Iterator<Map.Entry<Offer, List<CalendarConfig>>> iterator = offerConfigMap.entrySet().iterator();
        //for every offer
        while (iterator.hasNext()){
            Map.Entry<Offer, List<CalendarConfig>> entry = iterator.next();
            Offer offer = entry.getKey();
            List<CalendarConfig> configsForOffer = entry.getValue();
            CalendarConfig firstConfig = configsForOffer.get(0);
            
            //make sure the first configuration starts before the requested booking time
            if (selectedTime.compareTo(firstConfig.getStartTime()) < 0){
                continue;
            }
            
            LocalTime endTime = null;
            Integer duration = firstConfig.getMinDuration();
                
            BigDecimal basePricePerMinute;
            BigDecimal price = null;
            
            Map<Integer, BigDecimal> durationPriceMap = new TreeMap<>();
            Boolean isContiguous = true;
            for (CalendarConfig config: configsForOffer){
                
                //make sure there is no gap between calendar configurations
                if (endTime == null){
                    //first run
                    endTime = selectedTime.plusMinutes(config.getMinDuration());
                } else {
                    //break if there are durations available and calendar configs are not contiguous
                    if (!durationPriceMap.isEmpty()){
                        //we substract min interval before the comparison as it has been added during the last iteration
                        if (!endTime.minusMinutes(firstConfig.getMinInterval()).equals(config.getStartTime())){
                            break;
                        }
                    }
                }
                
                Integer interval = firstConfig.getMinInterval();
            
                basePricePerMinute = config.getBasePrice().divide(new BigDecimal(firstConfig.getMinDuration().toString()), MathContext.DECIMAL128);
                
                //as long as the endTime is before the end time configured in the calendar
                while (endTime.compareTo(config.getEndTime()) <= 0) {
                    TimeSlot timeSlot = new TimeSlot();
                    timeSlot.setDate(selectedDate);
                    timeSlot.setStartTime(selectedTime);
                    timeSlot.setEndTime(endTime);
                    timeSlot.setConfig(config);
                    Long bookingSlotsLeft = bookingUtil.getBookingSlotsLeft(timeSlot, offer, confirmedBookings);

                    //we only allow contiguous bookings for any given offer
                    if (bookingSlotsLeft<1){
                        isContiguous = false;
                        break;
                    }
                    
                    if (price == null){
                        //initilze price based on min duration
                        price = basePricePerMinute.multiply(new BigDecimal(duration.toString()), MathContext.DECIMAL128);
                    } else {
                        //add price for additional interval
                        price = price.add(basePricePerMinute.multiply(new BigDecimal(interval.toString()), MathContext.DECIMAL128));
                    }
                    durationPriceMap.put(duration, price);

                    //increase the duration by the configured minimum interval and determine the new end time for the next iteration
                    duration += interval;
                    endTime = endTime.plusMinutes(interval);
                }
                
                if (!durationPriceMap.isEmpty()){
                    OfferDurationPrice odp = new OfferDurationPrice();
                    odp.setOffer(offer);
                    odp.setDurationPriceMap(durationPriceMap);
                    odp.setConfig(config);
                    offerDurationPrices.add(odp);
                }
                
                if (!isContiguous){
                    //we only allow coniguous bookings for one offer. process next offer
                    break;
                }
            }
        }
        return offerDurationPrices;
    }
}
