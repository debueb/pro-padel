package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.GenericDAO;
import de.appsolve.padelcampus.db.model.EventGroup;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

;

/**
 * @author dominik
 */
@Component
public class EventGroupDAO extends GenericDAO<EventGroup> implements EventGroupDAOI {

    @Override
    public List<EventGroup> findAll() {
        List<EventGroup> eventGropus = super.findAll();
        Collections.sort(eventGropus);
        return eventGropus;
    }

}