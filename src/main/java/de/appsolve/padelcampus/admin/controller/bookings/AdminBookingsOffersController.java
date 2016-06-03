/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.bookings;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/bookings/offers")
public class AdminBookingsOffersController extends AdminBaseController<Offer> {
    
    @Autowired
    BaseEntityDAOI<Offer> offerDAO;
    
    @Override
    public ModelAndView showAddView(){
        Offer newOffer = createNewInstance();
        Long position = 0L;
        for (Offer offer: offerDAO.findAll()){
            position = Math.max(position, offer.getPosition()+1);
        }
        newOffer.setPosition(position);
        return getEditView(newOffer);
    }
   
    @Override
    public BaseEntityDAOI getDAO() {
        return offerDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/bookings/offers";
    }
}
