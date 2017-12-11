/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.db.model.Participant;
import org.joda.time.LocalDate;

import java.util.List;

/**
 * @author dominik
 */
public interface GameDAOI extends BaseEntityDAOI<Game> {

    Game findByIdFetchWithNextGame(Long id);

    Game findByIdFetchWithEventAndNextGame(Long id);

    Game findByIdFetchWithTeamsAndScoreReporter(Long id);

    Game findByIdFetchWithEventAndTeamsAndScoreReporter(Long id);

    List<Game> findByEvent(Event event);

    List<Game> findByEventWithPlayers(Event event);

    List<Game> findByParticipant(Participant participant);

    List<Game> findByParticipantAndEvent(Participant participant, Event event);

    List<Game> findByParticipantAndEventWithScoreOnly(Participant participant, Event event);

    List<Game> findAllYoungerThanForGenderWithPlayers(LocalDate date, Gender gender);
}
