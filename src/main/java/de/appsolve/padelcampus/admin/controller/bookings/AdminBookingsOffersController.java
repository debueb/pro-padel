/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.bookings;

import de.appsolve.padelcampus.admin.controller.general.AdminSortableController;
import de.appsolve.padelcampus.db.dao.OfferDAOI;
import de.appsolve.padelcampus.db.dao.OfferOptionDAOI;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.Offer;
import de.appsolve.padelcampus.spring.OfferOptionCollectionEditor;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/bookings/offers")
public class AdminBookingsOffersController extends AdminSortableController<Offer> {
    
    @Autowired
    OfferDAOI offerDAO;
    
    @Autowired
    OfferOptionDAOI offerOptionDAO;
    
    @Autowired
    OfferOptionCollectionEditor offerOptionCollectionEditor;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Set.class, "offerOptions", offerOptionCollectionEditor);
    }
    
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
    protected ModelAndView getEditView(Offer model){
        ModelAndView editView = super.getEditView(model);
        editView.addObject("OfferOptions", offerOptionDAO.findAll());
        return editView;
    }
   
    @Override
    public BaseEntityDAOI getDAO() {
        return offerDAO;
    }
    
    @Override
    protected Offer findById(Long modelId) {
        return offerDAO.findByIdFetchWithOfferOptions(modelId);
    }

    @Override
    public String getModuleName() {
        return "admin/bookings/offers";
    }
}
