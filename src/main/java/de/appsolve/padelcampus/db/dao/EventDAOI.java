/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Team;
import org.hibernate.criterion.Criterion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * @author dominik
 */
public interface EventDAOI extends BaseEntityDAOI<Event> {

    Event findByIdFetchWithGames(Long id);

    Event findByIdFetchWithParticipants(Long id);

    Event findByIdFetchWithParticipantsAndGames(Long id);

    Event findByIdFetchWithParticipantsAndPlayers(Long id);

    List<Event> findByParticipant(Participant participant);

    List<Event> findAllFetchWithParticipants();

    Page<Event> findAllFetchWithParticipantsAndPlayers(Pageable pageable);

    Page<Event> findAllFetchWithParticipantsAndPlayers(Pageable pageable, Set<Criterion> criterions);

    List<Event> findAllUpcomingWithParticipant(Team team);

    List<Event> findAllActive();

    List<Event> findAllActiveFetchWithParticipantsAndPlayers();
}
