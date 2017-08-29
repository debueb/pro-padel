/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.reports;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.DateRange;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/reports/utilization")
public class AdminReportsUtilizationController extends BaseController {

    @Autowired
    BookingDAOI bookingDAO;

    @Autowired
    ObjectMapper objectMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
    }

    @RequestMapping()
    public ModelAndView getIndex() throws JsonProcessingException {
        LocalDate endDate = new LocalDate();
        LocalDate startDate = endDate.minusMonths(3);
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
        ModelAndView mav = new ModelAndView("admin/reports/utilization/index");
        List<Booking> bookings = bookingDAO.findBlockedBookingsBetween(dateRange.getStartDate(), dateRange.getEndDate());

        Map<Long, Integer> map = new TreeMap<>();
        for (Booking booking : bookings) {
            LocalDate date = booking.getBookingDate();
            Long millis = date.toDateTimeAtStartOfDay().getMillis();
            Integer count = map.get(millis);
            if (count == null) {
                count = 1;
            } else {
                count++;
            }
            map.put(millis, count);
        }

        mav.addObject("chartData", objectMapper.writeValueAsString(map));
        mav.addObject("DateRange", dateRange);

        return mav;
    }

}
