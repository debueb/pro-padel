/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.spring;

import de.appsolve.padelcampus.db.dao.EventDAOI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author dominik
 */
@Component
public class EventCollectionEditor extends CustomCollectionEditor {

    @Autowired
    EventDAOI eventDAO;

    public EventCollectionEditor() {
        super(Set.class);
    }

    @Override
    protected Object convertElement(Object element) {
        if (element == null || !(element instanceof String)) {
            return null;
        }
        return eventDAO.findById(Long.valueOf((String) element));
    }
}
