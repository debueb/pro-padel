/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.db.model.GameSet;
import de.appsolve.padelcampus.db.model.Participant;

import java.util.List;

/**
 * @author dominik
 */
public interface GameSetDAOI extends BaseEntityDAOI<GameSet> {

    List<GameSet> findByParticipant(Participant participant);

    List<GameSet> findByGame(Game game);

    List<GameSet> findByEvent(Event event);

    List<GameSet> findBy(Game game, Participant participant);

    GameSet findBy(Game game, Participant participant, Integer setNumber);
}
