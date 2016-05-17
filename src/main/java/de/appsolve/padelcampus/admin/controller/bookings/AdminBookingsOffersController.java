/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.bookings;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/bookings/offers")
public class AdminBookingsOffersController extends AdminBaseController<Offer> {
    
    @Autowired
    PlayerDAOI playerDAO;
    
    @Autowired
    BaseEntityDAOI<Offer> offerDAO;
   
    @Override
    public BaseEntityDAOI getDAO() {
        return offerDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/bookings/offers";
    }
}
