/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.spring;

import de.appsolve.padelcampus.db.dao.OfferDAOI;
import de.appsolve.padelcampus.db.model.Offer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

/**
 * @author dominik
 */
@Component
public class OfferPropertyEditor extends PropertyEditorSupport {

    private static final Logger LOG = Logger.getLogger(OfferPropertyEditor.class);

    @Autowired
    OfferDAOI offerDAO;

    @Override
    public void setAsText(String text) {
        try {
            Long id = Long.parseLong(text);
            Offer model = offerDAO.findByIdFetchWithOfferOptions(id);
            setValue(model);
        } catch (NumberFormatException e) {
            LOG.error(e);
            setValue(null);
        }
    }
}
