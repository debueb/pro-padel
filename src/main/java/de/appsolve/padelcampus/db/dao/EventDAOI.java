/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Participant;
import java.util.List;
import java.util.Set;
import org.hibernate.criterion.Criterion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author dominik
 */
public interface EventDAOI extends BaseEntityDAOI<Event>{
    
    Event findByIdFetchWithGames(Long id);
    
    Event findByIdFetchWithParticipants(Long id);
    
    Event findByIdFetchWithParticipantsAndGames(Long id);
    
    Event findByIdFetchWithParticipantsAndPlayers(Long id);
    
    public List<Event> findAllFetchWithParticipants();
    
    Page<Event> findAllFetchWithParticipantsAndPlayers(Pageable pageable);
    
    Page<Event> findAllFetchWithParticipantsAndPlayers(Pageable pageable, Set<Criterion> criterions);
    
    List<Event> findAllFetchWithParticipantsAndPlayers();
    
    List<Event> findAllWithParticipant(Participant participant);

    public List<Event> findAllActive();

    public List<Event> findAllActiveFetchWithParticipantsAndPlayers();
}
