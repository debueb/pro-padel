/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.events;

import de.appsolve.padelcampus.constants.BookingType;
import de.appsolve.padelcampus.constants.EventType;
import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.controller.bookings.BookingsPayDirektController;
import de.appsolve.padelcampus.controller.bookings.BookingsPayMillController;
import de.appsolve.padelcampus.controller.bookings.BookingsPayPalController;
import de.appsolve.padelcampus.controller.bookings.BookingsVoucherController;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.*;
import de.appsolve.padelcampus.exceptions.MailException;
import de.appsolve.padelcampus.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/events/bookings")
public class EventsBookingController extends BaseController {

    private static final Logger LOG = Logger.getLogger(EventsBookingController.class);

    @Autowired
    EventDAOI eventDAO;

    @Autowired
    SessionUtil sessionUtil;

    @Autowired
    PlayerDAOI playerDAO;

    @Autowired
    Validator validator;

    @Autowired
    BookingDAOI bookingDAO;

    @Autowired
    BookingsPayPalController bookingsPayPalController;

    @Autowired
    BookingsPayDirektController bookingsPayDirektController;

    @Autowired
    BookingsPayMillController bookingsPayMillController;

    @Autowired
    BookingsVoucherController bookingsVoucherController;

    @Autowired
    BookingUtil bookingUtil;

    @Autowired
    EventsUtil eventsUtil;

    @Autowired
    TeamDAOI teamDAO;

    @Autowired
    GameUtil gameUtil;

    @RequestMapping(method = GET, value = "/{eventId}/participate")
    public ModelAndView participate(@PathVariable("eventId") Long eventId, HttpServletRequest request) {
        Player user = sessionUtil.getUser(request);
        if (user == null) {
            return getLoginRequiredView(request, msg.get("Participate"));
        }
        return participateView(eventId, new Player());
    }

    @RequestMapping(method = POST, value = "/{eventId}/participate")
    public ModelAndView postParticipate(@PathVariable("eventId") Long eventId, HttpServletRequest request, final @ModelAttribute("Player") Player player, BindingResult bindingResult) {
        ModelAndView participateView = participateView(eventId, player);
        try {
            Player user = sessionUtil.getUser(request);
            if (user == null) {
                return getLoginRequiredView(request, msg.get("Participate"));
            }

            Event event = eventDAO.findByIdFetchWithParticipants(eventId);

            Booking booking = new Booking();
            booking.setPlayer(user);
            booking.setBookingDate(event.getStartDate());
            booking.setBookingTime(event.getStartTime());
            booking.setAmount(event.getPrice());
            booking.setCurrency(event.getCurrency());
            booking.setPaymentMethod(PaymentMethod.valueOf(request.getParameter("paymentMethod")));
            booking.setBookingType(BookingType.loggedIn);
            booking.setEvent(event);

            switch (event.getEventType()) {
                case PullRoundRobin:
                    break;
                default:
                    Player partner;
                    if (player.getUUID() == null) {
                        validator.validate(player, bindingResult);
                        if (bindingResult.hasErrors()) {
                            return participateView;
                        }
                        if (playerDAO.findByEmail(player.getEmail()) != null) {
                            throw new Exception(msg.get("EmailAlreadyRegistered"));
                        }
                        partner = playerDAO.saveOrUpdate(player);
                    } else {
                        partner = playerDAO.findByUUID(player.getUUID());
                        if (partner == null) {
                            throw new Exception(msg.get("ChoosePartner"));
                        }
                    }

                    Set<Player> participants = new HashSet<>();
                    //do not add user as this would cause duplicate key (player and players go into the same table)
                    participants.add(partner);

                    //extra fields
                    booking.setPlayers(participants);
            }
            isEventBookingPossible(booking);

            sessionUtil.setBooking(request, booking);
            return new ModelAndView("redirect:/events/bookings/" + event.getId() + "/confirm");
        } catch (Exception e) {
            bindingResult.addError(new ObjectError("*", e.getMessage()));
            return participateView;
        }
    }


    @RequestMapping(value = "{eventId}/confirm")
    public ModelAndView showConfirmView(HttpServletRequest request) {
        Booking booking = sessionUtil.getBooking(request);
        ModelAndView confirmView = getBookingConfirmView(booking);
        try {
            if (booking == null || booking.getPlayer() == null) {
                throw new Exception(msg.get("SessionTimeout"));
            }
            isEventBookingPossible(booking);
        } catch (Exception e) {
            LOG.warn("Error while processing booking request: " + e.getMessage(), e);
            confirmView.addObject("error", e.getMessage());
            return confirmView;
        }
        return confirmView;
    }

    @RequestMapping(value = "{eventId}/confirm", method = POST)
    public ModelAndView confirmBooking(HttpServletRequest request) throws Exception {
        Booking booking = sessionUtil.getBooking(request);
        ModelAndView confirmView = getBookingConfirmView(booking);
        try {
            if (booking == null) {
                throw new Exception(msg.get("SessionTimeout"));
            }

            String publicBooking = request.getParameter("public-booking");
            Boolean isPublicBooking = !StringUtils.isEmpty(publicBooking) && publicBooking.equalsIgnoreCase("on");
            booking.setPublicBooking(isPublicBooking);

            String cancellationPolicyCheckbox = request.getParameter("accept-cancellation-policy");
            if (StringUtils.isEmpty(cancellationPolicyCheckbox) || !cancellationPolicyCheckbox.equals("on")) {
                throw new Exception(msg.get("BookingCancellationPolicyNotAccepted"));
            }

            if (booking.getConfirmed()) {
                throw new Exception(msg.get("BookingAlreadyConfirmed"));
            }

            isEventBookingPossible(booking);

            booking.setBlockingTime(new LocalDateTime());
            booking.setUUID(BookingUtil.generateUUID());
            bookingDAO.saveOrUpdate(booking);

            switch (booking.getPaymentMethod()) {
                case Cash:
                    if (booking.getConfirmed()) {
                        throw new Exception(msg.get("BookingAlreadyConfirmed"));
                    }
                    return new ModelAndView("redirect:/events/bookings/" + booking.getUUID() + "/success");
                case PayPal:
                    return bookingsPayPalController.redirectToPaypal(booking, request);
                case PayDirekt:
                    return bookingsPayDirektController.redirectToPayDirekt(booking, request);
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
            LOG.warn("Error while processing booking request: " + e.getMessage(), e);
            confirmView.addObject("error", e.getMessage());
            return confirmView;
        }
    }

    @RequestMapping(value = "/{UUID}/success")
    public ModelAndView showSuccessView(@PathVariable("UUID") String UUID, HttpServletRequest request) {
        ModelAndView mav = getBookingSuccessView();
        Booking booking = bookingDAO.findByUUIDWithEventAndPlayers(UUID);
        try {
            if (!booking.getPaymentMethod().equals(PaymentMethod.Cash) && !booking.getPaymentConfirmed()) {
                throw new Exception(msg.get("PaymentHasNotBeenConfirmed"));
            }

            if (booking.getConfirmed()) {
                throw new Exception(msg.get("BookingAlreadyConfirmed"));
            }

            Event event = eventDAO.findByIdFetchWithParticipantsAndGames(booking.getEvent().getId());

            switch (event.getEventType()) {
                case PullRoundRobin:
                    event.getParticipants().add(booking.getPlayer());
                    event = eventDAO.saveOrUpdate(event);

                    //eventsUtil.createPullGames(event);
                    break;
                default:
                    Set<Player> players = new HashSet<>();
                    players.add(booking.getPlayer());
                    players.addAll(booking.getPlayers());
                    //figure out if team already exists
                    Team team = teamDAO.findByPlayers(players);

                    //create team if it does not exist
                    if (team == null) {
                        team = new Team();
                        team.setPlayers(players);
                        team.setName(TeamUtil.getTeamName(team));
                        teamDAO.saveOrUpdate(team);
                    }

                    //add team to participant list
                    event.getParticipants().add(team);
                    event = eventDAO.saveOrUpdate(event);

                    if (event.getEventType().equals(EventType.SingleRoundRobin)) {
                        gameUtil.createMissingGames(event, event.getParticipants());
                    }
            }

            booking.setConfirmed(true);
            bookingDAO.saveOrUpdate(booking);

            bookingUtil.sendEventBookingConfirmation(request, booking);
            booking.setConfirmationMailSent(true);
            bookingDAO.saveOrUpdate(booking);

            bookingUtil.sendNewBookingNotification(request, booking);
        } catch (MailException | IOException ex) {
            LOG.error("Error while sending booking confirmation email", ex);
            mav.addObject("error", msg.get("FailedToSendBookingConfirmationEmail", new Object[]{FormatUtils.DATE_MEDIUM.print(booking.getBookingDate()), FormatUtils.TIME_HUMAN_READABLE.print(booking.getBookingTime())}));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            mav.addObject("error", e.getMessage());
        }
        return mav;
    }

    @RequestMapping(value = "/{UUID}/abort")
    public ModelAndView abortBooking(@PathVariable("UUID") String UUID, HttpServletRequest request) {
        Booking booking = bookingDAO.findByUUIDWithEvent(UUID);
        booking.setBlockingTime(null);
        booking.setCancelled(Boolean.TRUE);
        booking.setCancelReason("User cancelled during payment process");
        bookingDAO.saveOrUpdate(booking);
        return new ModelAndView("redirect:/events/event/" + booking.getEvent().getId());
    }

    private ModelAndView participateView(Long eventId, Player player) {
        Event event = eventDAO.findById(eventId);
        ModelAndView mav;
        switch (event.getEventType()) {
            case PullRoundRobin:
                mav = new ModelAndView("events/bookings/participate/pull");
                break;
            default:
                mav = new ModelAndView("events/bookings/participate/index");
        }
        mav.addObject("Model", event);
        mav.addObject("Player", player);
        return mav;
    }

    private ModelAndView getBookingConfirmView(Booking booking) {
        ModelAndView mav = new ModelAndView("events/bookings/confirm");
        mav.addObject("Booking", booking);
        return mav;
    }

    private ModelAndView getBookingSuccessView() {
        return new ModelAndView("events/bookings/success");
    }

    private void isEventBookingPossible(Booking booking) throws Exception {
        Event event = booking.getEvent();
        Event eventWithPlayers = eventDAO.findByIdFetchWithParticipantsAndPlayers(event.getId());
        checkPlayerNotParticipating(eventWithPlayers, booking.getPlayer());
        if (booking.getPlayers() != null) {
            for (Player player : booking.getPlayers()) {
                checkPlayerNotParticipating(eventWithPlayers, player);
            }
        }
        if (event.getParticipants().size() >= event.getMaxNumberOfParticipants()) {
            throw new Exception(msg.get("EventBookedOut"));
        }
    }

    private void checkPlayerNotParticipating(Event event, Player user) throws Exception {
        for (Participant p : event.getParticipants()) {
            if (p instanceof Team) {
                Team team = (Team) p;
                if (team.getPlayers().contains(user)) {
                    throw new Exception(msg.get("AlreadyParticipatesInThisEvent", new Object[]{user}));
                }
            } else {
                if (p.equals(user)) {
                    throw new Exception(msg.get("AlreadyParticipatesInThisEvent", new Object[]{user}));
                }
            }
        }
    }
}
