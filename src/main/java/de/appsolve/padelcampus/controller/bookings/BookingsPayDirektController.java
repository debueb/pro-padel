/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.bookings;

import de.appsolve.padelcampus.db.dao.PayDirektConfigDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.PayDirektConfig;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/bookings")
public class BookingsPayDirektController extends BookingsPaymentController {
    
    @Autowired
    PayDirektConfigDAOI payDirektConfigDAO;

    ModelAndView redirectToPayDirekt(Booking booking, HttpServletRequest request) throws Exception {
        if (booking.getPaymentConfirmed()){
            return BookingsController.getRedirectToSuccessView(booking);
        }
        PayDirektConfig payDirektConfig = payDirektConfigDAO.findFirst();
        throw new Exception("PayDirekt Zahlungen noch nicht implementiert.");
    }
    
}
