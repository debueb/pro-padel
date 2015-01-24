/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Participant;
import java.util.List;

/**
 *
 * @author dominik
 */
public interface EventDAOI extends GenericDAOI<Event>{
    
    List<Event> findAllWithParticipant(Participant participant);

    public List<Event> findAllActive();
}
