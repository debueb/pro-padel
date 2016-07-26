/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.bookings;

import de.appsolve.padelcampus.constants.PayDirektEndpoint;
import de.appsolve.padelcampus.db.dao.PayDirektConfigDAOI;
import de.appsolve.padelcampus.db.model.PayDirektConfig;
import de.appsolve.padelcampus.utils.Msg;
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
@RequestMapping("/admin/bookings/paydirekt")
public class AdminBookingsPayDirektController{
    
    @Autowired
    Msg msg;
    
    @Autowired
    PayDirektConfigDAOI payDirektConfigDAO;
    
    @RequestMapping()
    public ModelAndView showSettings(){
        PayDirektConfig config = payDirektConfigDAO.findFirst();
        if (config == null){
            config = new PayDirektConfig();
        }
        return getConfigView(config);
    }
    
    @RequestMapping(method=POST)
    public ModelAndView saveSettings(@Valid @ModelAttribute("Model") PayDirektConfig config, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return getConfigView(config);
        }
        payDirektConfigDAO.saveOrUpdate(config);
        return new ModelAndView("redirect:/admin/bookings/");
        
    }

    private ModelAndView getConfigView(PayDirektConfig config) {
        ModelAndView configView = new ModelAndView("admin/bookings/paydirekt/index", "Model", config);
        configView.addObject("EndPoints", PayDirektEndpoint.values());
        return configView;
    }
}
