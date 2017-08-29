/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.spring;

import de.appsolve.padelcampus.db.dao.CalendarConfigDAOI;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

/**
 * @author dominik
 */
@Component
public class CalendarConfigPropertyEditor extends PropertyEditorSupport {

    @Autowired
    CalendarConfigDAOI calendarConfigDAO;

    @Override
    public void setAsText(String text) {
        try {
            Long id = Long.parseLong(text);
            CalendarConfig config = calendarConfigDAO.findById(id);
            setValue(config);
        } catch (NumberFormatException e) {
            setValue(null);
        }
    }
}
