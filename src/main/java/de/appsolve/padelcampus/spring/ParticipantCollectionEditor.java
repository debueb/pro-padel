/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.spring;

import de.appsolve.padelcampus.db.dao.ParticipantDAOI;
import de.appsolve.padelcampus.db.model.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author dominik
 */
@Component
public class ParticipantCollectionEditor extends CustomCollectionEditor {

    @Autowired
    ParticipantDAOI participantDAO;

    public ParticipantCollectionEditor() {
        super(Set.class);
    }

    @Override
    protected Object convertElement(Object element) {
        if (element == null || !(element instanceof String)) {
            return null;
        }
        Participant p = participantDAO.findByUUID((String) element);
        return p;
    }
}
