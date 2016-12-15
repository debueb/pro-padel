/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.spring;

import de.appsolve.padelcampus.constants.Privilege;
import de.appsolve.padelcampus.db.dao.OfferOptionDAOI;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class PrivilegeCollectionPropertyEditor extends CustomCollectionEditor {
    
    @Autowired
    OfferOptionDAOI offerOptionDAO;

    public PrivilegeCollectionPropertyEditor() {
        super(Set.class);
    }
    
    @Override
    protected Object convertElement(Object element) {
        return Privilege.valueOf((String) element);
    }
}
