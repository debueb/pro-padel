/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller;

import de.appsolve.padelcampus.constants.CalendarWeekDay;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.db.dao.CalendarConfigDAOI;
import de.appsolve.padelcampus.db.dao.GenericDAOI;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import de.appsolve.padelcampus.utils.HolidayUtil;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
    }
    
    @Override
    public ModelAndView showAddView(){
        return getEditView(getDefaultCalendarConfig());
    }
    
    @Override
    public ModelAndView postEditView(@ModelAttribute("Model") CalendarConfig model, HttpServletRequest request, BindingResult result){
        if (!result.hasErrors()){
            List<CalendarConfig> configs = calendarConfigDAO.findAll();
            for (CalendarConfig config: configs){
                if (!config.equals(model) && config.getPriority().equals(model.getPriority())){
                    result.addError(new ObjectError("priority", msg.get("PriorityAlreadyExists")));
                    break;
                }
            }
        }
        return super.postEditView(model, request, result);
    }
    
    @Override
    protected ModelAndView getEditView(CalendarConfig model){
        ModelAndView editView = new ModelAndView("/admin/"+getModuleName()+"/edit", "Model", model);
        editView.addObject("WeekDays", CalendarWeekDay.values());
        editView.addObject("HolidayKeys", HolidayUtil.getHolidayKeys());
        return editView;
    }
    
    private CalendarConfig getDefaultCalendarConfig() {
        CalendarConfig calendarConfig = new CalendarConfig();
        List<CalendarConfig> configs = calendarConfigDAO.findAll();
        calendarConfig.setPriority(configs.size()+1);
        calendarConfig.setCourtCount(Constants.BOOKING_DEFAULT_COURT_COUNT);
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
