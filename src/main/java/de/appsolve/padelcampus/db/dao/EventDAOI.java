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
    
    Event findByIdFetchWithParticipants(Long id);
    
    Event findByIdFetchWithParticipantsAndPlayers(Long id);
    
    Event findByIdFetchWithParticipantsAndPlayersAndGames(Long id);
    
    Event findByIdFetchWithParticipantsAndGames(Long id);
    
    List<Event> findAllFetchWithParticipants();
    
    List<Event> findAllFetchWithParticipantsAndPlayers();
    
    List<Event> findAllWithParticipant(Participant participant);

    public List<Event> findAllActive();
}
