/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.reports;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.utils.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/reports/paymentmethods")
public class AdminReportsTopPaymentMethodsController extends BaseController {

    @Autowired
    BookingDAOI bookingDAO;

    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping()
    public ModelAndView getIndex() throws JsonProcessingException {
        return getIndexView();
    }

    private ModelAndView getIndexView() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("admin/reports/paymentmethods/index");
        List<Booking> bookings = bookingDAO.findBlockedBookings();

        Map<String, Integer> map = new HashMap<>();
        for (Booking booking : bookings) {
            PaymentMethod method = booking.getPaymentMethod();
            String methodTranslated = msg.get(method.name());
            Integer count = map.get(methodTranslated);
            if (count == null) {
                count = 1;
            } else {
                count++;
            }
            map.put(methodTranslated, count);
        }

        Map<String, Integer> sortedMap = SortUtil.sortMap(map);
        mav.addObject("chartData", objectMapper.writeValueAsString(sortedMap));

        return mav;
    }

}
