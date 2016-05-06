/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.bookings;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.db.dao.generic.GenericDAOI;
import de.appsolve.padelcampus.db.model.Facility;
import de.appsolve.padelcampus.db.model.Offer;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
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
@RequestMapping("/admin/bookings/facilities")
public class AdminBookingsFacilitiesController extends AdminBaseController<Facility> {
    
    @Autowired
    GenericDAOI<Facility> facilityDAO;
    
    @Autowired
    GenericDAOI<Offer> offerDAO;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Set.class, "offers", new CustomCollectionEditor(Set.class) {
            @Override
            protected Object convertElement(Object element) {
                Long id = Long.parseLong((String) element);
                return offerDAO.findById(id);
            }
        });
    }
    
    @Override
    protected ModelAndView getEditView(Facility model){
        ModelAndView editView = super.getEditView(model);
        editView.addObject("Offers", offerDAO.findAll());
        return editView;
    }
   
    @Override
    public GenericDAOI getDAO() {
        return facilityDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/bookings/facilities";
    }
}