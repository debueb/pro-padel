/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Ranking;
import org.joda.time.LocalDate;

import java.util.List;

/**
 * @author dominik
 */
public interface RankingDAOI extends BaseEntityDAOI<Ranking> {

    List<Ranking> findByGenderAndDate(Gender gender, LocalDate date);

    List<Ranking> findByParticipantBetweenDates(Participant participant, Gender gender, LocalDate startDate, LocalDate endDate);
}
