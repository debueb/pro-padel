/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.admin.controller.invoices;

import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.MasterDataDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.MasterData;
import de.appsolve.padelcampus.utils.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller
@RequestMapping("/invoices")
public class InvoicesController {
    
    @Autowired
    BookingDAOI bookingDAO;
    
    @Autowired
    MasterDataDAOI masterDataDAO;
    
    @Autowired
    Msg msg;
    
    @RequestMapping(method = GET, value = "booking/{bookingUUID}")
    public ModelAndView getInvoid(@PathVariable("bookingUUID") String uuid){
        MasterData masterData = masterDataDAO.findFirst();
        if (masterData == null){
            return new ModelAndView("invoices/masterdata_missing");
        }
        Booking booking = bookingDAO.findByUUID(uuid);
        ModelAndView mav = new ModelAndView("invoices/invoice");
        mav.addObject("Booking", booking);
        mav.addObject("MasterData", masterData);
        return mav;
    }
}
