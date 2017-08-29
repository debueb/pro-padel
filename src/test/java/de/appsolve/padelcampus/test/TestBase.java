/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test;

import com.google.common.collect.Sets;
import de.appsolve.padelcampus.constants.*;
import de.appsolve.padelcampus.constants.Currency;
import de.appsolve.padelcampus.db.dao.*;
import de.appsolve.padelcampus.db.model.*;
import de.appsolve.padelcampus.utils.Msg;
import de.appsolve.padelcampus.utils.SessionUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.math.BigDecimal;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author dominik
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("test")
public abstract class TestBase {

    protected static final String ADMIN_EMAIL = "admin@pro-padel.de";
    protected static final String ADMIN_PASSWORD = "test";

    protected static final String USER_EMAIL = "user@pro-padel.de";
    protected static final String USER_PASSWORD = "test";
    protected static final Logger LOG = Logger.getLogger(TestBase.class);
    protected MockMvc mockMvc;
    protected SortedSet<Offer> offers;
    protected Offer offer1;
    protected Offer offer2;
    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    protected MockHttpSession session;
    @Autowired
    protected BookingDAOI bookingDAO;
    @Autowired
    protected PlayerDAOI playerDAO;
    @Autowired
    protected TeamDAOI teamDAO;
    @Autowired
    protected VoucherDAOI voucherDAO;
    @Autowired
    protected AdminGroupDAOI adminGroupDAO;
    @Autowired
    protected EventDAOI eventDAO;
    @Autowired
    protected CustomerDAOI customerDAO;
    @Autowired
    protected SubscriptionDAOI subscriptionDAO;
    @Autowired
    protected MatchOfferDAOI matchOfferDAO;
    @Autowired
    protected SessionUtil sessionUtil;
    @Autowired
    protected Msg msg;
    protected Customer customer;
    @Autowired
    private OfferDAOI offerDAO;
    @Autowired
    private CalendarConfigDAOI calendarConfigDAO;

    @Before
    public void setUp() {
        if (mockMvc == null) {
            LocaleContextHolder.setLocale(Constants.DEFAULT_LOCALE);

            bookingDAO.delete(bookingDAO.findAll());

            List<Event> events = eventDAO.findAllFetchWithParticipants();
            for (Event event : events) {
                event.getParticipants().clear();
                event = eventDAO.saveOrUpdate(event);
            }
            eventDAO.delete(events);

            subscriptionDAO.delete(subscriptionDAO.findAll());

            adminGroupDAO.delete(adminGroupDAO.findAll());

            teamDAO.delete(teamDAO.findAll());

            matchOfferDAO.delete(matchOfferDAO.findAll());

            playerDAO.delete(playerDAO.findAll());


            for (CalendarConfig config : calendarConfigDAO.findAll()) {
                calendarConfigDAO.deleteById(config.getId());
            }
            for (Voucher voucher : voucherDAO.findAll()) {
                voucherDAO.deleteById(voucher.getId());
            }
            for (Offer offer : offerDAO.findAll()) {
                offerDAO.deleteById(offer.getId());
            }

            customerDAO.delete(customerDAO.findAll());

            customer = new Customer();
            customer.setName("testCustomer");
            customer.setDomainNames(new HashSet<>(Arrays.asList(new String[]{"localhost"})));
            customer = customerDAO.saveOrUpdate(customer);
            session.setAttribute(Constants.SESSION_CUSTOMER, customer);

            offers = new TreeSet<>();
            offer1 = new Offer();
            offer1.setName("Platz 1");
            offer1.setMaxConcurrentBookings(1L);
            offer1.setPosition(0L);
            offerDAO.saveOrUpdate(offer1);
            offers.add(offer1);
            offer2 = new Offer();
            offer2.setName("Platz 2");
            offer2.setMaxConcurrentBookings(1L);
            offer2.setPosition(1L);
            offerDAO.saveOrUpdate(offer2);
            offers.add(offer2);
            CalendarConfig calendarConfig = new CalendarConfig();
            calendarConfig.setBasePrice(BigDecimal.TEN);
            calendarConfig.setCalendarWeekDays(new HashSet<>(Arrays.asList(new CalendarWeekDay[]{CalendarWeekDay.Monday})));
            calendarConfig.setOffers(offers);
            calendarConfig.setCurrency(Currency.EUR);
            calendarConfig.setEndDate(getNextMonday());
            calendarConfig.setEndTimeHour(21);
            calendarConfig.setEndTimeMinute(30);
            calendarConfig.setHolidayKey(Constants.NO_HOLIDAY_KEY);
            calendarConfig.setMinDuration(60);
            calendarConfig.setMinInterval(30);
            calendarConfig.setStartDate(getNextMonday());
            calendarConfig.setStartTimeHour(10);
            calendarConfig.setStartTimeMinute(00);
            calendarConfig.setPaymentMethods(new HashSet<>(Arrays.asList(PaymentMethod.values())));
            calendarConfigDAO.saveOrUpdate(calendarConfig);

            mockMvc = webAppContextSetup(this.wac)
                    .addFilter(new DelegatingFilterProxy("adminFilter", this.wac), "/admin/*")
                    .alwaysDo(print())
                    .build();
        }
    }

    protected LocalDate getNextMonday() {
        LocalDate d = new LocalDate();
        if (d.getDayOfWeek() >= DateTimeConstants.MONDAY) {
            d = d.plusWeeks(1);
        }
        return d.withDayOfWeek(DateTimeConstants.MONDAY);
    }

    protected LocalDate getLastMonday() {
        LocalDate d = new LocalDate();
        if (d.getDayOfWeek() >= DateTimeConstants.MONDAY) {
            d = d.minusWeeks(1);
        }
        return d.withDayOfWeek(DateTimeConstants.MONDAY);
    }

    protected void createAdminAccount() throws Exception {
        LOG.info("Creating new account " + ADMIN_EMAIL);
        mockMvc.perform(post("/login/register")
                .session(session)
                .param("firstName", "admin")
                .param("lastName", "test")
                .param("email", ADMIN_EMAIL)
                .param("phone", "01739398758")
                .param("password", ADMIN_PASSWORD))
                .andExpect(status().isOk());
        logout(); //registering a new account also logs the user in automatically

        LOG.info("Promoting account to admin");
        AdminGroup adminGroup = new AdminGroup();
        adminGroup.setName("admins");
        adminGroup.setPrivileges(Sets.newHashSet(Privilege.values()));
        Player admin = playerDAO.findByEmail(ADMIN_EMAIL);
        adminGroup.setPlayers(new TreeSet<>(Arrays.asList(new Player[]{admin})));
        adminGroupDAO.saveOrUpdate(adminGroup);
    }

    protected void logout() throws Exception {
        LOG.info("make sure user is not logged in");
        mockMvc.perform(get("/logout")
                .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        //logout invalidates the session. make sure to create a new one
        session = new MockHttpSession(wac.getServletContext());
    }

    protected void login(String email, String password) throws Exception {
        LOG.info("login");
        mockMvc.perform(post("/login?redirect=/matchoffers/add")
                .session(session)
                .param("email", email)
                .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/matchoffers/add"));

        Assert.assertNotNull(session);
        Assert.assertNotNull(session.getAttribute(Constants.SESSION_USER));
        Assert.assertNotNull(session.getAttribute(Constants.SESSION_PRIVILEGES));
    }
}
