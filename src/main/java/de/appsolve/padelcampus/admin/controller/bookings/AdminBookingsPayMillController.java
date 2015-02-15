/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.bookings;

import de.appsolve.padelcampus.db.dao.PayMillConfigDAOI;
import de.appsolve.padelcampus.db.model.PayMillConfig;
import de.appsolve.padelcampus.utils.Msg;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/bookings/paymill")
public class AdminBookingsPayMillController{
    
    @Autowired
    Msg msg;
    
    @Autowired
    PayMillConfigDAOI payMillConfigDAO;
    
    @RequestMapping()
    public ModelAndView showSettings(){
        PayMillConfig config = payMillConfigDAO.findFirst();
        if (config == null){
            config = new PayMillConfig();
        }
        return getConfigView(config);
    }
    
    @RequestMapping(method=POST)
    public ModelAndView saveSettings(@Valid @ModelAttribute("Model") PayMillConfig config, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return getConfigView(config);
        }
        payMillConfigDAO.saveOrUpdate(config);
        return new ModelAndView("redirect:/admin/bookings/");
    }

    private ModelAndView getConfigView(PayMillConfig config) {
        ModelAndView configView = new ModelAndView("admin/bookings/paymill/index", "Model", config);
        return configView;
    }
}
