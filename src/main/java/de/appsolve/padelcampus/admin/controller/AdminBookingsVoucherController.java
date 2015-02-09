/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller;

import de.appsolve.padelcampus.constants.CalendarWeekDay;
import static de.appsolve.padelcampus.constants.Constants.DEFAULT_TIMEZONE;
import static de.appsolve.padelcampus.constants.Constants.VOUCHER_DEFAULT_VALIDITY_IN_DAYS;
import static de.appsolve.padelcampus.constants.Constants.BOOKING_DEFAULT_VALID_FROM_HOUR;
import static de.appsolve.padelcampus.constants.Constants.BOOKING_DEFAULT_VALID_FROM_MINUTE;
import static de.appsolve.padelcampus.constants.Constants.BOOKING_DEFAULT_VALID_UNTIL_HOUR;
import static de.appsolve.padelcampus.constants.Constants.BOOKING_DEFAULT_VALID_UNTIL_MINUTE;
import de.appsolve.padelcampus.db.dao.GenericDAOI;
import de.appsolve.padelcampus.db.dao.OfferDAOI;
import de.appsolve.padelcampus.db.dao.VoucherDAOI;
import de.appsolve.padelcampus.db.model.Voucher;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import de.appsolve.padelcampus.utils.VoucherUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/bookings/voucher")
public class AdminBookingsVoucherController extends AdminBaseController<Voucher>{
    
    @Autowired
    VoucherDAOI voucherDAO;
    
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
        Voucher voucher = new Voucher();
        //TODO: use minDuration
        voucher.setDuration(60L);
        voucher.setValidUntil(new LocalDate(DEFAULT_TIMEZONE).plusDays(VOUCHER_DEFAULT_VALIDITY_IN_DAYS));
        voucher.setValidFromHour(BOOKING_DEFAULT_VALID_FROM_HOUR);
        voucher.setValidFromMinute(BOOKING_DEFAULT_VALID_FROM_MINUTE);
        voucher.setValidUntilHour(BOOKING_DEFAULT_VALID_UNTIL_HOUR);
        voucher.setValidUntilMinute(BOOKING_DEFAULT_VALID_UNTIL_MINUTE);
        voucher.setCalendarWeekDays(CalendarWeekDay.valuesAsSet());
        
        return getEditView(voucher);
    }
    
    @Override
    public ModelAndView getEditView(Voucher voucher){
        ModelAndView mav = super.getEditView(voucher);
        
        //TODO: calculate from minDuration + minInterval
        mav.addObject("Durations", new Integer[]{60,90,120});
        mav.addObject("WeekDays", CalendarWeekDay.values());
        mav.addObject("voucherCount", 1);
        mav.addObject("Offers", offerDAO.findAll());
        
        return mav;
    }
    
    @Override
    @RequestMapping(value={"add", "edit/{modelId}"}, method=POST)
    public ModelAndView postEditView(@ModelAttribute("Model") Voucher model, HttpServletRequest request, BindingResult result){
        ModelAndView editView = getEditView(model);
        Integer voucherCount = Integer.parseInt(request.getParameter("voucherCount"));
        editView.addObject("voucherCount", voucherCount);
        validator.validate(model, result);
        if (!result.hasErrors()){
            List<Voucher> vouchers = new ArrayList<>();
            for (int i=0; i<voucherCount; i++){
                Voucher voucher = VoucherUtil.createNewVoucher(model);
                voucherDAO.saveOrUpdate(voucher);
                vouchers.add(voucher);
            }

            editView.addObject("Vouchers", vouchers);
        }
        return editView;
    }
    
    @Override
    public GenericDAOI getDAO() {
        return voucherDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/bookings/voucher";
    }
}
