/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.constants.EventType;
import de.appsolve.padelcampus.db.dao.generic.GenericDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Participant;
import java.util.List;

/**
 *
 * @author dominik
 */
public interface EventDAOI extends GenericDAOI<Event>{
    
    Event findByIdFetchWithGames(Long id);
    
    Event findByIdFetchWithParticipants(Long id);
    
    Event findByIdFetchWithParticipantsAndGames(Long id);
    
    Event findByIdFetchWithParticipantsAndPlayers(Long id);
    
    List<Event> findAllFetchWithParticipants();
    
    List<Event> findAllFetchWithParticipantsAndPlayers();
    
    List<Event> findAllWithParticipant(Participant participant);

    public List<Event> findAllActive();

    public List<Event> findAllActiveFetchWithParticipantsAndPlayers();
}
