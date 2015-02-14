/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.reports;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/reports/times")
public class AdminReportsTimesController extends BaseController{

    @Autowired
    BookingDAOI bookingDAO;
    
    @RequestMapping()
    public ModelAndView getIndex() throws JsonProcessingException{
        return getIndexView();
    }
    
    private ModelAndView getIndexView() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("admin/reports/times/index");
        return mav;
    }
}
