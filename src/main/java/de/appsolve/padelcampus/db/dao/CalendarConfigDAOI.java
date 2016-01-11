/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.GenericDAOI;
import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.exceptions.CalendarConfigException;
import java.util.List;
import org.joda.time.LocalDate;

/**
 *
 * @author dominik
 */
public interface CalendarConfigDAOI extends GenericDAOI<CalendarConfig>{
    
    List<CalendarConfig> findBetween(LocalDate startDate, LocalDate endDate);
    List<CalendarConfig> findFor(LocalDate date) throws CalendarConfigException;
}
