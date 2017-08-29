/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.bookings;

import de.appsolve.padelcampus.admin.controller.general.AdminSortableController;
import de.appsolve.padelcampus.constants.OfferOptionType;
import de.appsolve.padelcampus.db.dao.OfferOptionDAOI;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.OfferOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/bookings/offeroptions")
public class AdminBookingsOfferOptionsController extends AdminSortableController<OfferOption> {

    @Autowired
    OfferOptionDAOI offerOptionDAO;

    @Override
    protected ModelAndView getIndexView(Page<OfferOption> page) {
        ModelAndView indexView = super.getIndexView(page);
        indexView.addObject("OfferOptionTypes", OfferOptionType.values());
        return indexView;
    }

    @Override
    public ModelAndView showAddView(HttpServletRequest request) {
        OfferOption newOffer = createNewInstance();
        Long position = 0L;
        for (OfferOption offer : offerOptionDAO.findAll()) {
            position = Math.max(position, offer.getPosition() + 1);
        }
        newOffer.setPosition(position);
        return getEditView(newOffer);
    }

    @Override
    public BaseEntityDAOI getDAO() {
        return offerOptionDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/bookings/offeroptions";
    }
}
