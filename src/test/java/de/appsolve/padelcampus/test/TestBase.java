/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test;

import de.appsolve.padelcampus.constants.CalendarWeekDay;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.constants.Currency;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.CalendarConfigDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.db.model.Player;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author dominik
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"/testContext.xml"})
public abstract class TestBase extends TestCase {
    
    protected MockMvc mockMvc;
    
    protected MockHttpSession session;

    protected static final Logger log = Logger.getLogger(TestBase.class);
    
    @Autowired
    private WebApplicationContext wac;
    
    @Autowired
    private MockHttpSession _session;

    @Autowired
    private CalendarConfigDAOI calendarConfigDAO;
    
    @Autowired
    BookingDAOI bookingDAO;
    
    @Autowired
    PlayerDAOI playerDAO;
    
    @Before
    @Override
    public void setUp() {
        if (mockMvc == null){
            CalendarConfig calendarConfig = new CalendarConfig();
            calendarConfig.setBasePrice(BigDecimal.TEN);
            calendarConfig.setCalendarWeekDays(new HashSet<>(Arrays.asList(new CalendarWeekDay[]{CalendarWeekDay.Monday})));
            calendarConfig.setCourtCount(2);
            calendarConfig.setCurrency(Currency.EUR);
            calendarConfig.setEndDate(getNextMonday());
            calendarConfig.setEndTimeHour(21);
            calendarConfig.setEndTimeMinute(30);
            calendarConfig.setHolidayKey(Constants.NO_HOLIDAY_KEY);
            calendarConfig.setMinDuration(60);
            calendarConfig.setMinInterval(30);
            calendarConfig.setPriority(0);
            calendarConfig.setStartDate(getNextMonday());
            calendarConfig.setStartTimeHour(10);
            calendarConfig.setStartTimeMinute(00);
            calendarConfigDAO.saveOrUpdate(calendarConfig);
            
            mockMvc = webAppContextSetup(this.wac).build();
            session = _session;
        }
        List<Booking> bookings = bookingDAO.findAll();
        for(Booking booking: bookings){
            bookingDAO.deleteById(booking.getId());
        }
        
        List<Player> players = playerDAO.findAll();
        for (Player player: players){
            playerDAO.deleteById(player.getId());
        }
    }
    
    protected LocalDate getNextMonday() {
        LocalDate d = new LocalDate();
        if (d.getDayOfWeek() >= DateTimeConstants.MONDAY) {
            d = d.plusWeeks(1);
        }
        return d.withDayOfWeek(DateTimeConstants.MONDAY);
    }
}
