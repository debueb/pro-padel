/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.spring;

import de.appsolve.padelcampus.db.dao.ParticipantDAOI;
import de.appsolve.padelcampus.db.model.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

/**
 * @author dominik
 */
@Component
public class PlayerPropertyEditor extends PropertyEditorSupport {

    @Autowired
    ParticipantDAOI participantDAO;

    @Override
    public void setAsText(String text) {
        try {
            Participant model = participantDAO.findByUUID(text);
            setValue(model);
        } catch (NumberFormatException e) {
            setValue(null);
        }
    }
}
