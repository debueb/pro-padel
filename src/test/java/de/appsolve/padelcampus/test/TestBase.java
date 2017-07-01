/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test;

import com.google.common.collect.Sets;
import de.appsolve.padelcampus.constants.CalendarWeekDay;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.constants.Currency;
import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.constants.Privilege;
import de.appsolve.padelcampus.db.dao.AdminGroupDAOI;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.CalendarConfigDAOI;
import de.appsolve.padelcampus.db.dao.CustomerDAOI;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.MatchOfferDAOI;
import de.appsolve.padelcampus.db.dao.OfferDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.dao.SubscriptionDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.dao.VoucherDAOI;
import de.appsolve.padelcampus.db.model.AdminGroup;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.db.model.Customer;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Offer;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Voucher;
import de.appsolve.padelcampus.utils.Msg;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 *
 * @author dominik
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfig.class, loader = AnnotationConfigWebContextLoader.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class TestBase  {
    
    protected static final String ADMIN_EMAIL     = "admin@appsolve.de";
    protected static final String ADMIN_PASSWORD  = "test";
    
    protected static final String USER_EMAIL     = "user@appsolve.de";
    protected static final String USER_PASSWORD  = "test";
    
    protected MockMvc mockMvc;
    
    protected SortedSet<Offer> offers;
    
    protected Offer offer1;
    
    protected Offer offer2;

    protected static final Logger LOG = Logger.getLogger(TestBase.class);
    
    @Autowired
    protected WebApplicationContext wac;
    
    @Autowired
    protected MockHttpSession session;
    
    @Autowired
    private OfferDAOI offerDAO;

    @Autowired
    private CalendarConfigDAOI calendarConfigDAO;
    
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

    @Before
    public void setUp() {
        if (mockMvc == null){
            LocaleContextHolder.setLocale(Constants.DEFAULT_LOCALE);
        
            bookingDAO.delete(bookingDAO.findAll());

            List<Event> events = eventDAO.findAllFetchWithParticipants();
            for (Event event: events){
                event.getParticipants().clear();
                event = eventDAO.saveOrUpdate(event);
            }
            eventDAO.delete(events);
            
            subscriptionDAO.delete(subscriptionDAO.findAll());
            
            adminGroupDAO.delete(adminGroupDAO.findAll());

            teamDAO.delete(teamDAO.findAll());
            
            matchOfferDAO.delete(matchOfferDAO.findAll());

            playerDAO.delete(playerDAO.findAll());
            
            
            for (CalendarConfig config: calendarConfigDAO.findAll()){
                calendarConfigDAO.deleteById(config.getId());
            }
            for (Voucher voucher: voucherDAO.findAll()){
                voucherDAO.deleteById(voucher.getId());
            }
            for (Offer offer: offerDAO.findAll()){
                offerDAO.deleteById(offer.getId());
            }
            
            customerDAO.delete(customerDAO.findAll());
            
            customer = new Customer();
            customer.setName("testCustomer");
            customer.setDomainNames(new HashSet<>(Arrays.asList(new String[]{"localhost"})));
            customer  = customerDAO.saveOrUpdate(customer);
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
            
            mockMvc =   webAppContextSetup(this.wac)
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
    
    protected void createAdminAccount() throws Exception{
        LOG.info("Creating new account "+ADMIN_EMAIL);
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
