package de.appsolve.padelcampus.test.bookings;

import de.appsolve.padelcampus.constants.BookingType;
import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Subscription;
import de.appsolve.padelcampus.test.TestBase;
import jersey.repackaged.com.google.common.collect.Sets;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;

import static de.appsolve.padelcampus.constants.Constants.SESSION_BOOKING;
import static de.appsolve.padelcampus.constants.Constants.SESSION_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author dominik
 */
public class BookingSubscriptionTest extends TestBase {

    private static final String EXISTING_USER_FIRST_NAME = BookingSubscriptionTest.class.getSimpleName() + "_firstName";
    private static final String EXISTING_USER_LAST_NAME = BookingSubscriptionTest.class.getSimpleName() + "_lastName";
    private static final String EXISTING_USER_EMAIL = BookingSubscriptionTest.class.getSimpleName() + "_lastName@pro-padel.de";
    private static final String EXISTING_USER_PASSWORD = BookingSubscriptionTest.class.getSimpleName() + "_password";

    private static final String NO_LOGIN_USER_FIRST_NAME = BookingSubscriptionTest.class.getSimpleName() + "_firstName_noLogin";
    private static final String NO_LOGIN_USER_LAST_NAME = BookingSubscriptionTest.class.getSimpleName() + "_lastName_noLogin";
    private static final String NO_LOGIN_USER_EMAIL = BookingSubscriptionTest.class.getSimpleName() + "_lastName_noLogin@pro-padel.de";

    private static final String REGISTER_USER_FIRST_NAME = BookingSubscriptionTest.class.getSimpleName() + "_firstName_register";
    private static final String REGISTER_USER_LAST_NAME = BookingSubscriptionTest.class.getSimpleName() + "_lastName_register";
    private static final String REGISTER_USER_EMAIL = BookingSubscriptionTest.class.getSimpleName() + "_lastName_register@pro-padel.de";
    private static final String REGISTER_USER_PASSWORD = BookingSubscriptionTest.class.getSimpleName() + "_password_register";


    @Before
    @Override
    public void setUp() {
        super.setUp();
        Subscription subscription = new Subscription();
        subscription.setCustomer(customer);
        subscription.setMaxMinutesPerDay(60);
        subscription.setMaxMinutesPerWeek(60);
        subscription.setName("Membership");
        subscription.setPrice(BigDecimal.ONE);
        Player player = new Player();
        player.setCustomer(customer);
        player.setFirstName(EXISTING_USER_FIRST_NAME);
        player.setLastName(EXISTING_USER_LAST_NAME);
        player.setEmail(EXISTING_USER_EMAIL);
        player.setPassword(EXISTING_USER_PASSWORD);
        player.setPhone("017473932943");
        player = playerDAO.saveOrUpdate(player);
        subscription.setPlayers(Sets.newHashSet(player));
        subscriptionDAO.saveOrUpdate(subscription);
    }

    @Test
    public void bookWithSubscriptionAndLogin() throws Exception {
        LOG.info("Test booking workflow [paymentMethod: Subscription, bookingType: login]");

        mockMvc.perform(get("/bookings/" + getNextMonday() + "/10:00/offer/" + offer1.getId())
                .session(session))
                .andExpect(view().name("bookings/booking"))
                .andExpect(model().hasNoErrors());

        mockMvc.perform(post("/bookings/" + getNextMonday() + "/10:00/offer/" + offer1.getId())
                .session(session)
                .param("bookingTime", "10:00")
                .param("offer", offer1.getId().toString())
                .param("bookingType", BookingType.login.name())
                .param("duration", "60")
                .param("paymentMethod", PaymentMethod.Subscription.name())
                .param("accept-tac", "on")
                .param("accept-pp", "on"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/login"));

        Booking booking = (Booking) session.getAttribute(SESSION_BOOKING);
        Assert.notNull(booking, "Booking must not be null");

        mockMvc.perform(post("/login")
                .session(session)
                .param("firstName", EXISTING_USER_FIRST_NAME)
                .param("lastName", EXISTING_USER_LAST_NAME)
                .param("email", EXISTING_USER_EMAIL)
                .param("phone", "01739398758")
                .param("password", EXISTING_USER_PASSWORD))
                .andExpect(redirectedUrl("/bookings/" + getNextMonday() + "/10:00/offer/" + offer1.getId()));

        Player user = (Player) session.getAttribute(SESSION_USER);
        Assert.notNull(user, "user should be logged in");
        Assert.isTrue(!user.getGuest(), "user should not be a guest");

        mockMvc.perform(post("/bookings/" + getNextMonday() + "/10:00/offer/" + offer1.getId())
                .session(session)
                .param("bookingTime", "10:00")
                .param("offer", offer1.getId().toString())
                .param("bookingType", BookingType.loggedIn.name())
                .param("duration", "60")
                .param("paymentMethod", PaymentMethod.Subscription.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/bookings/" + getNextMonday() + "/10:00/confirm"));

        mockMvc.perform(get("/bookings/" + getNextMonday() + "/10:00/confirm")
                .session(session))
                .andExpect(view().name("bookings/confirm"))
                .andExpect(model().hasNoErrors());

        booking = (Booking) session.getAttribute(SESSION_BOOKING);
        Assert.notNull(booking.getPlayer(), "Player must not be null");

        mockMvc.perform(post("/bookings/" + getNextMonday() + "/10:00/confirm")
                .session(session)
                .param("accept-cancellation-policy", "on"))
                .andExpect(redirectedUrl("/bookings/booking/" + booking.getUUID() + "/success"))
                .andExpect(model().hasNoErrors());

        List<Booking> bookings = bookingDAO.findAll();
        Assert.notNull(bookings, "Bookings must not be null");
        Assert.notEmpty(bookings, "Bookings must not be empty");
        Assert.isTrue(bookings.contains(booking), "Bookings must contain booking");
    }

    @Test
    public void overbookWithSubscriptionAndLoggedIn() throws Exception {
        LOG.info("Test booking workflow [paymentMethod: Subscription, bookingType: loggedIn, scenario: overbooking subscription time]");

        //create first booking
        bookWithSubscriptionAndLogin();

        //second booking
        LocalDate nextMonday = getNextMonday();

        mockMvc.perform(get("/bookings/" + nextMonday + "/11:00/offer/" + offer1.getId())
                .session(session))
                .andExpect(view().name("bookings/booking"))
                .andExpect(model().hasNoErrors());

        mockMvc.perform(post("/bookings/" + nextMonday + "/11:00/offer/" + offer1.getId())
                .session(session)
                .param("bookingTime", "11:00")
                .param("offer", offer1.getId().toString())
                .param("bookingType", BookingType.loggedIn.name())
                .param("duration", "60")
                .param("paymentMethod", PaymentMethod.Subscription.name()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void bookWithSubscriptionAndNoLogin() throws Exception {
        LOG.info("Test booking workflow [paymentMethod: Subscription, bookingType: nologin]");

        mockMvc.perform(get("/bookings/" + getNextMonday() + "/10:00/offer/" + offer1.getId())
                .session(session))
                .andExpect(view().name("bookings/booking"))
                .andExpect(model().hasNoErrors());

        mockMvc.perform(post("/bookings/" + getNextMonday() + "/10:00/offer/" + offer1.getId())
                .session(session)
                .param("bookingTime", "10:00")
                .param("offer", offer1.getId().toString())
                .param("bookingType", BookingType.nologin.name())
                .param("duration", "60")
                .param("paymentMethod", PaymentMethod.Subscription.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/bookings/nologin"));

        Booking booking = (Booking) session.getAttribute(SESSION_BOOKING);
        Assert.notNull(booking, "Booking must not be null");

        mockMvc.perform(post("/bookings/nologin")
                .session(session)
                .param("firstName", NO_LOGIN_USER_FIRST_NAME)
                .param("lastName", NO_LOGIN_USER_LAST_NAME)
                .param("email", NO_LOGIN_USER_EMAIL)
                .param("phone", "01739398758")
                .param("accept-tac", "on")
                .param("accept-pp", "on"))
                .andExpect(redirectedUrl("/bookings/" + getNextMonday() + "/10:00/offer/" + offer1.getId()));

        Player user = (Player) session.getAttribute(SESSION_USER);
        Assert.notNull(user, "user should be logged in");
        Assert.isTrue(user.getGuest(), "user should be guest");

        mockMvc.perform(post("/bookings/" + getNextMonday() + "/10:00/offer/" + offer1.getId())
                .session(session)
                .param("bookingTime", "10:00")
                .param("offer", offer1.getId().toString())
                .param("bookingType", BookingType.loggedIn.name())
                .param("duration", "60")
                .param("paymentMethod", PaymentMethod.Subscription.name()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void bookWithSubscriptionAndRegistration() throws Exception {
        LOG.info("Test booking workflow [paymentMethod: Subscription, bookingType: register]");

        mockMvc.perform(get("/bookings/" + getNextMonday() + "/10:00/offer/" + offer1.getId())
                .session(session))
                .andExpect(view().name("bookings/booking"))
                .andExpect(model().hasNoErrors());

        mockMvc.perform(post("/bookings/" + getNextMonday() + "/10:00/offer/" + offer1.getId())
                .session(session)
                .param("bookingTime", "10:00")
                .param("offer", offer1.getId().toString())
                .param("bookingType", BookingType.register.name())
                .param("duration", "60")
                .param("paymentMethod", PaymentMethod.Subscription.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/login/register"));

        Booking booking = (Booking) session.getAttribute(SESSION_BOOKING);
        Assert.notNull(booking, "Booking must not be null");

        mockMvc.perform(post("/login/register")
                .session(session)
                .param("firstName", REGISTER_USER_FIRST_NAME)
                .param("lastName", REGISTER_USER_LAST_NAME)
                .param("email", REGISTER_USER_EMAIL)
                .param("phone", "01739398758")
                .param("password", REGISTER_USER_PASSWORD)
                .param("accept-tac", "on")
                .param("accept-pp", "on"))
                .andExpect(redirectedUrl("/bookings/" + getNextMonday() + "/10:00/offer/" + offer1.getId()));

        Player user = (Player) session.getAttribute(SESSION_USER);
        Assert.notNull(user, "user should be logged in");
        Assert.isTrue(!user.getGuest(), "new registered user should not be a guest");

        mockMvc.perform(post("/bookings/" + getNextMonday() + "/10:00/offer/" + offer1.getId())
                .session(session)
                .param("bookingTime", "10:00")
                .param("offer", offer1.getId().toString())
                .param("bookingType", BookingType.loggedIn.name())
                .param("duration", "60")
                .param("paymentMethod", PaymentMethod.Subscription.name()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("error"));
    }
}
