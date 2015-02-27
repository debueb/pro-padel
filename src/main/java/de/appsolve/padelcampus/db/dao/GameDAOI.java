/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.db.model.Participant;
import java.util.List;

/**
 *
 * @author dominik
 */
public interface GameDAOI extends GenericDAOI<Game>{
    
    Game findByIdFetchWithTeams(Long id);
    List<Game> findByEvent(Event event);
    List<Game> findByParticipant(Participant participant);
    List<Game> findByParticipantAndEvent(Participant participant, Event event);
}
