/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.account;

import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.utils.Msg;
import de.appsolve.padelcampus.utils.SessionUtil;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/account/bookings")
public class AccountBookingsController extends BaseController {
    
    @Autowired
    PlayerDAOI playerDAO;
    
    @Autowired
    BookingDAOI bookingDAO;
    
    @Autowired
    SessionUtil sessionUtil;
    
    @Autowired
    Msg msg;
    
    @RequestMapping()
    public ModelAndView getIndex(HttpServletRequest request){
        return getIndexView(request);
    }
    
    @RequestMapping("booking/{UUID}")
    public ModelAndView getDetail(HttpServletRequest request, @PathVariable("UUID") String uuid){
        Booking booking = bookingDAO.findByUUID(uuid);
        return getDetailView(booking);
    }
    
    private ModelAndView getIndexView(HttpServletRequest request) {
        Player user = sessionUtil.getUser(request);
        if (user==null){
            return getLoginRequiredView(request, msg.get("MyBookings"));
        }
        ModelAndView view = new ModelAndView("account/bookings/index", "Model", user);
        view.addObject("Bookings", bookingDAO.findByPlayer(user));
        return view;
    }

    private ModelAndView getDetailView(Booking booking) {
        ModelAndView view = new ModelAndView("account/bookings/detail", "Booking", booking);
        return view;
    }
}
