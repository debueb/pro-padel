/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.bookings;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.constants.CalendarWeekDay;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.CalendarConfigDAOI;
import de.appsolve.padelcampus.db.dao.OfferDAOI;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.db.model.Offer;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import de.appsolve.padelcampus.utils.BookingUtil;
import de.appsolve.padelcampus.utils.HolidayUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/bookings/settings")
public class AdminBookingsSettingsController extends AdminBaseController<CalendarConfig> {

    @Autowired
    CalendarConfigDAOI calendarConfigDAO;

    @Autowired
    BookingDAOI bookingDAO;

    @Autowired
    OfferDAOI offerDAO;

    @Autowired
    BookingUtil bookingUtil;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));

        binder.registerCustomEditor(SortedSet.class, "offers", new CustomCollectionEditor(SortedSet.class) {
            @Override
            protected Object convertElement(Object element) {
                Long id = Long.parseLong((String) element);
                return offerDAO.findById(id);
            }
        });
    }

    @Override
    public ModelAndView showIndex(HttpServletRequest request, Pageable pageable, @RequestParam(required = false, name = "search") String search) {
        ModelAndView mav = new ModelAndView(getModuleName() + "/index");
        mav.addObject("Models", calendarConfigDAO.findAll());
        mav.addObject("moduleName", getModuleName());
        return mav;
    }

    @Override
    public ModelAndView showAddView(HttpServletRequest request) {
        return getEditView(getDefaultCalendarConfig());
    }

    @Override
    public ModelAndView postEditView(@ModelAttribute("Model") CalendarConfig config, HttpServletRequest request, BindingResult result) {

        //make sure no overlapping configurations are added
        List<CalendarConfig> existingConfigs = calendarConfigDAO.findAll();
        for (CalendarConfig existingConfig : existingConfigs) {
            //skip self
            if (config.getId() == null || !config.getId().equals(existingConfig.getId())) {
                //make sure this config starts before the exising config ends (date)
                if (config.getStartDate().compareTo(existingConfig.getEndDate()) < 0) {
                    //make sure this config ends after the existing config starts (date)
                    if (config.getEndDate().compareTo(existingConfig.getStartDate()) > 0) {
                        //make sure week day matches
                        for (CalendarWeekDay weekDay : config.getCalendarWeekDays()) {
                            for (CalendarWeekDay existingWeekDay : existingConfig.getCalendarWeekDays()) {
                                if (weekDay.equals(existingWeekDay)) {
                                    //make sure this config starts before the existing config ends (time)
                                    if (config.getStartTime().compareTo(existingConfig.getEndTime()) < 0) {
                                        //make sure this config ends after the exising config starts (time)
                                        if (config.getEndTime().compareTo(existingConfig.getStartTime()) > 0) {
                                            //make sure offer matches
                                            for (Offer offer : config.getOffers()) {
                                                for (Offer existingOffer : existingConfig.getOffers()) {
                                                    if (offer.equals(existingOffer)) {
                                                        result.addError(new ObjectError("id", msg.get("CannotAddCalendarConfigurationDueToOverlap")));
                                                        return super.postEditView(config, request, result);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return super.postEditView(config, request, result);
    }

    @Override
    protected ModelAndView getEditView(CalendarConfig model) {
        ModelAndView editView = new ModelAndView("/" + getModuleName() + "/edit", "Model", model);

        editView.addObject("PaymentMethods", bookingUtil.getActivePaymentMethods());
        editView.addObject("WeekDays", CalendarWeekDay.values());
        editView.addObject("HolidayKeys", HolidayUtil.getHolidayKeys());
        editView.addObject("Offers", offerDAO.findAll());
        return editView;
    }

    @Override
    public ModelAndView getDelete(HttpServletRequest request, @PathVariable("id") Long id) {
        CalendarConfig config = calendarConfigDAO.findById(id);
        ModelAndView mav = getDeleteView(config);
        //add warning about existing bookings that are affected by deleting this calendar configuration
        List<Booking> bookings = bookingDAO.findActiveBookingsBetween(config.getStartDate(), config.getEndDate());
        Iterator<Booking> iterator = bookings.iterator();
        while (iterator.hasNext()) {
            Booking booking = iterator.next();
            //remove bookings that do not match the week day
            CalendarWeekDay weekDay = CalendarWeekDay.values()[booking.getBookingDate().getDayOfWeek() - 1];
            if (!config.getCalendarWeekDays().contains(weekDay)) {
                iterator.remove();
                continue;
            }
            //remove bookings that start and end before the selected calendar config
            if (booking.getBookingTime().compareTo(config.getStartTime()) <= 0 && booking.getBookingEndTime().compareTo(config.getStartTime()) <= 0) {
                iterator.remove();
                continue;
            }
            //remove bookings that start after the selected calendar config ends
            if (booking.getBookingTime().compareTo(config.getEndTime()) >= 0) {
                iterator.remove();
                continue;
            }
            //remove bookings that do not match the offer
            if (booking.getOffer() != null) {
                if (!config.getOffers().contains(booking.getOffer())) {
                    iterator.remove();
                }
            }
        }
        if (!bookings.isEmpty()) {
            mav.addObject("error", msg.get("TheFollowingBookingsAreAffected", new Object[]{StringUtils.join(bookings, "<br/>")}));
        }
        return mav;
    }

    private CalendarConfig getDefaultCalendarConfig() {
        CalendarConfig calendarConfig = new CalendarConfig();
        LocalDate now = new LocalDate(Constants.DEFAULT_TIMEZONE);
        calendarConfig.setStartDate(now);
        calendarConfig.setEndDate(now.plusMonths(1));
        calendarConfig.setHolidayKey(Constants.DEFAULT_HOLIDAY_KEY);
        calendarConfig.setStartTimeHour(Constants.BOOKING_DEFAULT_VALID_FROM_HOUR);
        calendarConfig.setStartTimeMinute(Constants.BOOKING_DEFAULT_VALID_FROM_MINUTE);
        calendarConfig.setEndTimeHour(Constants.BOOKING_DEFAULT_VALID_UNTIL_HOUR);
        calendarConfig.setEndTimeMinute(Constants.BOOKING_DEFAULT_VALID_UNTIL_MINUTE);
        calendarConfig.setMinDuration(Constants.BOOKING_DEFAULT_DURATION);
        calendarConfig.setMinInterval(Constants.BOOKING_DEFAULT_MIN_INTERVAL);
        return calendarConfig;
    }

    @Override
    public BaseEntityDAOI getDAO() {
        return calendarConfigDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/bookings/settings";
    }
}
