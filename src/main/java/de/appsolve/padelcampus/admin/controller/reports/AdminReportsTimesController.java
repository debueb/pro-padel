/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.reports;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.appsolve.padelcampus.constants.CalendarWeekDay;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.DateRange;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.CalendarConfigDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import de.appsolve.padelcampus.utils.BookingUtil;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import static de.appsolve.padelcampus.utils.FormatUtils.TIME_HUMAN_READABLE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/reports/times")
public class AdminReportsTimesController extends BaseController {

    @Autowired
    BookingDAOI bookingDAO;

    @Autowired
    ObjectMapper exportObjectMapper;

    @Autowired
    BookingUtil bookingUtil;

    @Autowired
    CalendarConfigDAOI calendarConfigDAO;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
    }

    @RequestMapping(method = GET)
    public ModelAndView getIndex() throws JsonProcessingException {
        LocalDate endDate = new LocalDate();
        LocalDate startDate = endDate.minusYears(1);
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(startDate);
        dateRange.setEndDate(endDate);
        return getIndexView(dateRange);
    }

    @RequestMapping(method = POST)
    public ModelAndView postIndex(@ModelAttribute("DateRange") DateRange dateRange) throws JsonProcessingException {
        return getIndexView(dateRange);
    }

    private ModelAndView getIndexView(DateRange dateRange) throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("admin/reports/times/index");

        List<CalendarConfig> calendarConfigs = calendarConfigDAO.findBetween(dateRange.getStartDate(), dateRange.getEndDate());

        ArrayList<TimeHeatMapEntry> chartData = new ArrayList<>();
        for (CalendarConfig config : calendarConfigs) {
            Set<CalendarWeekDay> calendarWeekDays = config.getCalendarWeekDays();
            for (CalendarWeekDay weekDay : calendarWeekDays) {
                LocalTime startTime = config.getStartTime();
                LocalTime endTime = config.getEndTime();
                Integer minDuration = config.getMinDuration();
                while (startTime.plusMinutes(config.getMinDuration()).isBefore(endTime)) {
                    TimeHeatMapEntry entry = new TimeHeatMapEntry();
                    entry.setDayOfWeek(weekDay);
                    entry.setTime(startTime.toString(TIME_HUMAN_READABLE));
                    chartData.add(entry);
                    startTime = startTime.plusMinutes(minDuration);
                }
            }
        }

        List<Booking> bookings = bookingDAO.findActiveBookingsBetween(dateRange.getStartDate(), dateRange.getEndDate());
        for (Booking booking : bookings) {
            int dayOfWeek = booking.getBookingDate().getDayOfWeek();
            LocalTime bookingTime = booking.getBookingTime();
            TimeHeatMapEntry entry = new TimeHeatMapEntry();
            entry.setDayOfWeek(CalendarWeekDay.values()[dayOfWeek - 1]);
            entry.setTime(bookingTime.toString(TIME_HUMAN_READABLE));
            if (chartData.contains(entry)) {
                int index = chartData.indexOf(entry);
                entry = chartData.get(index);
                int count = entry.getCount() == null ? 0 : entry.getCount();
                entry.setCount(count + 1);
                chartData.add(index, entry);
            } else {
                //when calendar configurations were changed
                entry.setCount(1);
                chartData.add(entry);
            }
        }

        Collections.sort(chartData);
        mav.addObject("DateRange", dateRange);
        mav.addObject("chartData", exportObjectMapper.writeValueAsString(chartData));
        return mav;
    }
}
