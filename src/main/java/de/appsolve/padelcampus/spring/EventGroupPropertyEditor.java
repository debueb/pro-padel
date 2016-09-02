/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.spring;

import de.appsolve.padelcampus.db.dao.EventGroupDAOI;
import de.appsolve.padelcampus.db.model.EventGroup;
import java.beans.PropertyEditorSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class EventGroupPropertyEditor extends PropertyEditorSupport {

    @Autowired
    EventGroupDAOI eventGroupDAO;
    
    @Override
    public void setAsText(String text)
    {
        try {
            Long id = Long.parseLong(text);
            EventGroup config = eventGroupDAO.findById(id);
            setValue(config);
        } catch (NumberFormatException e){
            setValue(null);
        }
    }
}
