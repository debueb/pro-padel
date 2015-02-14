/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.bookings;

import de.appsolve.padelcampus.constants.PayPalEndpoint;
import de.appsolve.padelcampus.db.dao.PayPalConfigDAOI;
import de.appsolve.padelcampus.db.model.PayPalConfig;
import de.appsolve.padelcampus.utils.Msg;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/bookings/paypal")
public class AdminBookingsPayPalController{
    
    @Autowired
    Msg msg;
    
    @Autowired
    PayPalConfigDAOI payPalConfigDAO;
    
    @RequestMapping()
    public ModelAndView showSettings(){
        PayPalConfig config = payPalConfigDAO.findFirst();
        if (config == null){
            config = new PayPalConfig();
        }
        return getConfigView(config);
    }
    
    @RequestMapping(method=POST)
    public ModelAndView saveSettings(@Valid @ModelAttribute("Model") PayPalConfig config, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return getConfigView(config);
        }
        payPalConfigDAO.saveOrUpdate(config);
        return new ModelAndView("redirect:/admin/bookings/");
        
    }

    private ModelAndView getConfigView(PayPalConfig config) {
        ModelAndView configView = new ModelAndView("admin/bookings/paypal/index", "Model", config);
        configView.addObject("EndPoints", PayPalEndpoint.values());
        return configView;
    }
}
