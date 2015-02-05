/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller;

import de.appsolve.padelcampus.constants.CalendarWeekDay;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.CalendarConfigDAOI;
import de.appsolve.padelcampus.db.dao.GenericDAOI;
import de.appsolve.padelcampus.db.dao.OfferDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import de.appsolve.padelcampus.utils.HolidayUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/bookings/settings")
public class AdminBookingsSettingsController extends AdminBaseController<CalendarConfig>{
    
    @Autowired
    CalendarConfigDAOI calendarConfigDAO;
    
    @Autowired
    BookingDAOI bookingDAO;
    
    @Autowired
    OfferDAOI offerDAO;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
        
        binder.registerCustomEditor(Set.class, "offers", new CustomCollectionEditor(Set.class) {
            @Override
            protected Object convertElement(Object element) {
                Long id = Long.parseLong((String) element);
                return offerDAO.findById(id);
            }
        });
    }
    
    @Override
    public ModelAndView showAddView(){
        return getEditView(getDefaultCalendarConfig());
    }
    
    @Override
    public ModelAndView postEditView(@ModelAttribute("Model") CalendarConfig model, HttpServletRequest request, BindingResult result){
        if (!result.hasErrors()){
            //ToDo: make sure no overlapping configurations are added
            //result.addError(new ObjectError("priority", msg.get("PriorityAlreadyExists")));
        }
        return super.postEditView(model, request, result);
    }
    
    @Override
    protected ModelAndView getEditView(CalendarConfig model){
        ModelAndView editView = new ModelAndView("/admin/"+getModuleName()+"/edit", "Model", model);
        editView.addObject("WeekDays", CalendarWeekDay.values());
        editView.addObject("HolidayKeys", HolidayUtil.getHolidayKeys());
        editView.addObject("Offers", offerDAO.findAll());
        return editView;
    }
    
    @Override
    public ModelAndView getDelete(@PathVariable("id") Long id){
        CalendarConfig config = calendarConfigDAO.findById(id);
        ModelAndView mav = getDeleteView(config);
        //add warning about existing bookings that are affected by deleting this calendar configuration
        List<Booking> bookings = bookingDAO.findActiveBookingsBetween(config.getStartDate(), config.getEndDate());
        Iterator<Booking> iterator = bookings.iterator();
        while (iterator.hasNext()){
            Booking booking = iterator.next();
            //remove bookings that do not match the week day
            CalendarWeekDay weekDay = CalendarWeekDay.values()[booking.getBookingDate().getDayOfWeek()-1];
            if (!config.getCalendarWeekDays().contains(weekDay)){
                iterator.remove();
                continue;
            }
            //remove bookings that start and end before the selected calendar config
            if (booking.getBookingTime().isBefore(config.getStartTime()) && booking.getBookingEndTime().isBefore(config.getStartTime())){
                iterator.remove();
                continue;
            }
            //remove bookings that start after the selected calendar config
            if (booking.getBookingTime().isAfter(config.getEndTime())){
                iterator.remove();
            }
        }
        if (!bookings.isEmpty()){
            mav.addObject("error", msg.get("TheFollowingBookingsAreAffected", new Object[]{StringUtils.join(bookings, "<br/>")}));
        }
        return mav;
    }
    
    private CalendarConfig getDefaultCalendarConfig() {
        CalendarConfig calendarConfig = new CalendarConfig();
        LocalDate now = new LocalDate(Constants.DEFAULT_TIMEZONE);
        calendarConfig.setStartDate(now);
        calendarConfig.setEndDate(now.plusYears(1));
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
    public GenericDAOI getDAO() {
        return calendarConfigDAO;
    }

    @Override
    public String getModuleName() {
        return "bookings/settings";
    }
}
